import com.mongodb.MongoClient;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class PojoImmutableTest extends PojoTest {

    public static final PojoCodecProvider PROVIDER = PojoCodecProvider.builder()
            .automatic(true)
            .build();

    public static final CodecRegistry REGISTRY = fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            fromProviders(PROVIDER)
    );

    @Test
    public void test1() {

        Entity entity = new Entity("abc", 123, MyEnum.VAL1);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);
        System.out.println(out);

        Entity entity2 = readValue(out, Entity.class, REGISTRY);
        System.out.println(entity);

        Assert.assertEquals(entity, entity2);
    }

    @Test
    public void test2() {

        String in = "{ \"field1\" : \"def\", \"field2\" : 345, \"field3\" : \"VAL2\" }";
        Entity entity = readValue(in, Entity.class, REGISTRY);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);

        Assert.assertEquals(in, out);
    }


    public static enum MyEnum {

        VAL1,
        VAL2,
        VAL3

    }

    public static class Entity {

        private String field1;

        private int field2;

        private MyEnum field3;

        @BsonCreator
        public Entity(@BsonProperty("field1") String field1,
                      @BsonProperty("field2") int field2,
                      @BsonProperty("field3") MyEnum field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }

        public String getField1() {
            return field1;
        }

        public int getField2() {
            return field2;
        }

        public MyEnum getField3() {
            return field3;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "field1='" + field1 + '\'' +
                    ", field2=" + field2 +
                    ", field3=" + field3 +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entity entity = (Entity) o;
            return field2 == entity.field2 &&
                    Objects.equals(field1, entity.field1) &&
                    field3 == entity.field3;
        }

        @Override
        public int hashCode() {

            return Objects.hash(field1, field2, field3);
        }
    }

}