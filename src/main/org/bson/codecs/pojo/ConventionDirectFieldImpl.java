package org.bson.codecs.pojo;

import org.bson.codecs.configuration.CodecConfigurationException;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.*;

public class ConventionDirectFieldImpl implements Convention {

    @Override
    public void apply(final ClassModelBuilder<?> classModelBuilder) {
        Set<String> fieldsToRemove = new HashSet<>();
        for (PropertyModelBuilder<?> propertyModelBuilder : classModelBuilder.getPropertyModelBuilders()) {
            if (!(propertyModelBuilder.getPropertyAccessor() instanceof PropertyAccessorImpl)) {
                throw new CodecConfigurationException(format("The DIRECT_FIELDS_CONVENTION is not compatible with "
                                + "propertyModelBuilder instance that have custom implementations of org.bson.codecs.pojo.PropertyAccessor: %s",
                        propertyModelBuilder.getPropertyAccessor().getClass().getName()));
            }
            PropertyAccessorImpl<?> defaultAccessor = (PropertyAccessorImpl<?>) propertyModelBuilder.getPropertyAccessor();
            PropertyMetadata<?> propertyMetaData = defaultAccessor.getPropertyMetadata();
            Field field = propertyMetaData.getField();
            if (field != null) {
                final int modifiers = field.getModifiers();
                if (!isFinal(field.getModifiers()) &&
                        !isTransient(modifiers) && !isStatic(modifiers) &&
                        isPrivate(field.getModifiers())) {
                    setPropertyAccessor(propertyModelBuilder);
                }
            } else {
                // there's a setter but no field with this name
                fieldsToRemove.add(propertyMetaData.getName());
            }
        }
        // remove all properties for fields that don't exist
        for (String fieldToRemove : fieldsToRemove) {
            classModelBuilder.removeProperty(fieldToRemove);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void setPropertyAccessor(final PropertyModelBuilder<T> propertyModelBuilder) {
        propertyModelBuilder.propertyAccessor(new PrivateProperyAccessor<T>(
                (PropertyAccessorImpl<T>) propertyModelBuilder.getPropertyAccessor()));
    }

    private static final class PrivateProperyAccessor<T> implements PropertyAccessor<T> {

        private final PropertyAccessorImpl<T> wrapped;
        private final Field declaredField;

        private PrivateProperyAccessor(final PropertyAccessorImpl<T> wrapped) {
            this.wrapped = wrapped;
            try {
                Field field = wrapped.getPropertyMetadata().getField();
                declaredField = field.getDeclaringClass().getDeclaredField(field.getName());
                declaredField.setAccessible(true);
            } catch (Exception e) {
                throw new CodecConfigurationException(format("Unable to make private field accessible '%s' in %s",
                        wrapped.getPropertyMetadata().getName(), wrapped.getPropertyMetadata().getDeclaringClassName()), e);
            }
        }

        @Override
        public <S> T get(final S instance) {
            try {
                return (T) declaredField.get(instance);
            } catch (IllegalAccessException e) {
                throw new CodecConfigurationException(format("Unable to get value for property '%s' in %s",
                        wrapped.getPropertyMetadata().getName(), wrapped.getPropertyMetadata().getDeclaringClassName()), e);
            }
        }

        @Override
        public <S> void set(final S instance, final T value) {
            try {
                declaredField.set(instance, value);
            } catch (Exception e) {
                throw new CodecConfigurationException(format("Unable to set value for property '%s' in %s",
                        wrapped.getPropertyMetadata().getName(), wrapped.getPropertyMetadata().getDeclaringClassName()), e);
            }
        }
    }
}
