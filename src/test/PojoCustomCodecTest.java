import com.mongodb.MongoClient;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Tests a very simple class (no nested fields) with custom codec and no error handling
 */
public class PojoCustomCodecTest extends PojoTest {

    public static final CodecRegistry REGISTRY = fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            fromCodecs(new EntityCodec())
    );

    @Test
    public void test1() {

        String id = new ObjectId().toString();
        System.out.println("id=" + id);

        Entity entity = new Entity(id);
        entity.setField(123);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);
        System.out.println(out);

        Entity entity2 = readValue(out, Entity.class, REGISTRY);
        System.out.println(entity);

        Assert.assertEquals(entity, entity2);
    }

    @Test
    public void test2() {

        String in = "{ \"_id\" : { \"$oid\" : \"1234567890abcdef12345678\" }, \"field\" : \"345\" }";
        Entity entity = readValue(in, Entity.class, REGISTRY);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);

        Assert.assertEquals(in, out);
    }


    public static class Entity {

        /**
         * Represent the ID as a string in the Java class, but we'll serialize as an ObjectId
         */
        private String id;

        /**
         * Represent as an integer in Java, but serialize as a string
         */
        private int field;

        @BsonCreator
        public Entity(@BsonProperty("id") String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public int getField() {
            return field;
        }

        public void setField(int field) {
            this.field = field;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "id='" + id + '\'' +
                    ", field=" + field +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entity entity = (Entity) o;
            return field == entity.field &&
                    Objects.equals(id, entity.id);
        }

        @Override
        public int hashCode() {

            return Objects.hash(id, field);
        }
    }

    /**
     * Notice that the codec converts strings to ObjectId and reads string into an integer field
     */
    public static class EntityCodec implements Codec<Entity> {
        @Override
        public Entity decode(BsonReader reader, DecoderContext decoderContext) {
            Entity entity = null;
            reader.readStartDocument();
            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                String name = reader.readName();
                switch (name) {
                    case "_id":
                        entity = new Entity(reader.readObjectId().toString());
                        break;
                    case "field":
                        entity.setField(Integer.parseInt(reader.readString()));
                        break;
                    default:
                        throw new RuntimeException("Unexpected field: " + name);
                }
            }
            reader.readEndDocument();
            return entity;
        }

        @Override
        public void encode(BsonWriter writer, Entity value, EncoderContext encoderContext) {
            writer.writeStartDocument();
            writer.writeName("_id");
            writer.writeObjectId(new ObjectId(value.getId()));
            writer.writeName("field");
            writer.writeString("" + value.getField());
            writer.writeEndDocument();
        }

        @Override
        public Class<Entity> getEncoderClass() {
            return Entity.class;
        }
    }

}