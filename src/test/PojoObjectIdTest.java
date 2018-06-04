import com.mongodb.MongoClient;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.*;
import org.bson.codecs.pojo.annotations.SerializeToObjectId;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Test serialization of string ID field into native BSON ObjectId
 */
public class PojoObjectIdTest extends PojoTest {

    public static final PojoCodecProvider PROVIDER = PojoCodecProvider.builder()
            // .automatic(true)
            .register(Entity.class, EntityBytes.class)
            .conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION,
                    Conventions.CLASS_AND_PROPERTY_CONVENTION,
                    new ConventionObjectId())) // this will make direct access possible
            .build();

    public static final CodecRegistry REGISTRY = fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            fromProviders(PROVIDER)
            // fromCodecs(new ObjectIdCodec())
    );

    @Test
    public void test1() {

        Entity entity = new Entity();
        entity.setId("1234567890abcdef12345678");
        entity.setField(123);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);
        System.out.println(out);

        Assert.assertTrue("Missing ObjectId markup: " + out, out.contains("$oid"));

        Entity entity2 = readValue(out, Entity.class, REGISTRY);
        System.out.println(entity);

        Assert.assertEquals(entity, entity2);
    }

    @Test
    public void test1b() {

        ObjectId id = new ObjectId();

        EntityBytes entity = new EntityBytes();
        entity.setId(id.toByteArray());
        entity.setField(123);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);
        System.out.println(out);

        Assert.assertTrue("Missing ObjectId markup: " + out, out.contains("$oid"));
        Assert.assertTrue("Missing ObjectId value: " + out, out.contains(id.toHexString()));

        EntityBytes entity2 = readValue(out, EntityBytes.class, REGISTRY);
        System.out.println(entity);

        Assert.assertEquals(entity, entity2);
    }

    @Test
    public void test2() {

        String in = "{ \"_id\" : { \"$oid\" : \"1234567890abcdef12345678\" }, \"field\" : -345 }";
        Entity entity = readValue(in, Entity.class, REGISTRY);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);

        Assert.assertEquals(in, out);
    }


    @Test
    public void test2b() {

        String in = "{ \"_id\" : { \"$oid\" : \"1234567890abcdef12345678\" }, \"field\" : -345 }";
        EntityBytes entity = readValue(in, EntityBytes.class, REGISTRY);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);

        Assert.assertEquals(in, out);
    }


    public static class Entity {

        @SerializeToObjectId
        private String id;

        private int field;

        public Entity() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getField() {
            return field;
        }

        public void setField(int field) {
            this.field = field;
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

        @Override
        public String toString() {
            return "Entity{" +
                    "id='" + id + '\'' +
                    ", field=" + field +
                    '}';
        }
    }


    public static class EntityBytes {

        @SerializeToObjectId
        private byte[] id;

        private int field;

        public EntityBytes() {
        }

        public byte[] getId() {
            return id;
        }

        public void setId(byte[] id) {
            this.id = id;
        }

        public int getField() {
            return field;
        }

        public void setField(int field) {
            this.field = field;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EntityBytes that = (EntityBytes) o;
            return field == that.field &&
                    Arrays.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, field);
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "id='" + id + '\'' +
                    ", field=" + field +
                    '}';
        }
    }


}