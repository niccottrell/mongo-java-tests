import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class PojoCompoundIdTest extends PojoTest {

    @Test
    public void test1() {

        EntityKey key = new EntityKey("abc", new Date(2018, 5, 21));
        Entity entity = new Entity(key);
        entity.setValue("hello!");

        String in = "{ \"_id\" : {\"code\": \"abc\", \"date\": \"2018-05-21\"}, \"value\": \"hello!\"}";

        Entity entity2 = readValue(in, Entity.class);
        Assert.assertEquals(entity, entity2);

        String out = writeValueAsString(entity);
        assertThat(in, equalToIgnoringCase(out));
    }

    public class Entity {

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
    }

    public class EntityKey {

        final String code;

        final Date date;

        public EntityKey(String code, Date date) {
            this.code = code;
            this.date = date;
        }

        public String getCode() {
            return code;
        }

        public Date getDate() {
            return date;
        }
    }


}