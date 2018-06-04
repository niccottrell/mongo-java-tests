package org.bson.codecs.pojo.annotations;

/**
 *
 */

import java.lang.annotation.*;

/**
 * Indicates field that should be serialized as an ObjectId
 *
 * <p>Note: Requires {@link org.bson.codecs.pojo.ConventionObjectId}</p>
 *
 * @since 3.5
 * @see org.bson.codecs.pojo.Conventions#ANNOTATION_CONVENTION
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SerializeToObjectId {

}
