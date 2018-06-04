package org.bson.codecs.pojo;

import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.pojo.annotations.SerializeToObjectId;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static java.lang.String.format;

public class ConventionObjectId implements Convention {

    @Override
    public void apply(final ClassModelBuilder<?> classModelBuilder) {
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
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().isAssignableFrom(SerializeToObjectId.class)) {
                        propertyModelBuilder.codec(new ObjectIdCodec(field.getType()));
                    }
                }
            }
        }
    }

}
