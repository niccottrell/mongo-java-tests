package org.bson.codecs.pojo;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

public class ObjectIdCodec<T> implements Codec<T> {

    Class<T> clazz;

    public ObjectIdCodec(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        ObjectId objectId = reader.readObjectId();
        if (clazz.getName().equals("[B")) {
            return (T) objectId.toByteArray();
        } else if (clazz.isAssignableFrom(String.class)) {
            return (T) objectId.toHexString();
        } else if (clazz.isAssignableFrom(ObjectId.class)) {
            return (T) objectId;
        } else {
            throw new RuntimeException("Can't convert to " + clazz + ": " + objectId);
        }

    }

    @Override
    public void encode(BsonWriter writer, Object value, EncoderContext encoderContext) {
        if (clazz.getName().equals("[B")) {
            byte[] bytes = (byte[]) value;
            writer.writeObjectId(new ObjectId(bytes));
        } else if (clazz.isAssignableFrom(String.class)) {
            writer.writeObjectId(new ObjectId((String) value));
        } else if (clazz.isAssignableFrom(ObjectId.class)) {
            writer.writeObjectId((ObjectId) value);
        } else {
            throw new RuntimeException("Can't convert to ObjectId: " + value);
        }
    }

    @Override
    public Class getEncoderClass() {
        return null;
    }
}
