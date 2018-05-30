import codec.DateAsStringCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Objects;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class PojoCompoundIdTest extends PojoTest {

    @Test
    public void test1() {

        // include custom codec
        final CodecRegistry registry = fromRegistries(
                CodecRegistries.fromCodecs(new DateAsStringCodec()),
                CODEC_REGISTRY
        );

        String in = "{ \"_id\" : { \"code\" : \"abc\", \"date\" : \"2018-05-21\" }, \"value\" : \"hello!\" }";

        EntityKey key = new EntityKey("abc", new Date(2018-1900, 5 - 1, 21));
        Entity entity = new Entity(key);
        entity.setValue("hello!");

        String out = writeValueAsString(entity, registry);
        Assert.assertEquals(in, out);

        Entity entity2 = readValue(in, Entity.class, registry);
        Assert.assertEquals(entity, entity2);

    }


    public static class EntityKey {

        final String code;

        final Date date;

        @BsonCreator
        public EntityKey(@BsonProperty("code") String code, @BsonProperty("date") Date date) {
            this.code = code;
            this.date = date;
        }

        public String getCode() {
            return code;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EntityKey entityKey = (EntityKey) o;
            return Objects.equals(code, entityKey.code) &&
                    Objects.equals(date, entityKey.date);
        }

        @Override
        public int hashCode() {

            return Objects.hash(code, date);
        }
    }

    public static class Entity {

        @BsonId
        private final EntityKey key;

        private String value;

        @BsonCreator
        public Entity(@BsonProperty("key") EntityKey key) {
            this.key = key;
        }

        public EntityKey getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entity that = (Entity) o;
            return Objects.equals(key, that.key) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {

            return Objects.hash(key, value);
        }
    }
}