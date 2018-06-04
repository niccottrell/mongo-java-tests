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

/**
 * Tests an edge case of Immutable object with a *private* constructor.
 * Need to move @BsonCreator and @BsonProperty to static of() method.
 */
public class PojoImmutablePrivateConstructorTest extends PojoTest {

    private static final PojoCodecProvider PROVIDER = PojoCodecProvider.builder()
            .automatic(true)
            .build();

    private static final CodecRegistry REGISTRY = fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            fromProviders(PROVIDER)
    );

    @Test
    public void test1() {

        Entity entity = new Entity("abc", 123);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);
        System.out.println(out);

        Entity entity2 = readValue(out, Entity.class, REGISTRY);
        System.out.println(entity);

        Assert.assertEquals(entity, entity2);
    }

    @Test
    public void test2() {

        String in = "{ \"field1\" : \"def\", \"field2\" : 345 }";
        Entity entity = readValue(in, Entity.class, REGISTRY);
        System.out.println(entity);

        String out = writeValueAsString(entity, REGISTRY);

        Assert.assertEquals(in, out);
    }

    @SuppressWarnings("unused")
    public static class Entity {

        private String field1;

        private int field2;

        private Entity(String field1, int field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public String getField1() {
            return field1;
        }

        public int getField2() {
            return field2;
        }


        @BsonCreator
        public static Entity of(@BsonProperty("field1") String field1,
                                @BsonProperty("field2") int field2) {
            return new Entity(field1, field2);
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "field1='" + field1 + '\'' +
                    ", field2=" + field2 +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entity entity = (Entity) o;
            return field2 == entity.field2 &&
                    Objects.equals(field1, entity.field1);
        }

        @Override
        public int hashCode() {

            return Objects.hash(field1, field2);
        }
    }

}