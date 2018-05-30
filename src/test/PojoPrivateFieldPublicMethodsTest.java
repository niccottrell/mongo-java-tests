import org.junit.Assert;
import org.junit.Test;

public class PojoPrivateFieldPublicMethodsTest extends PojoTest {

    @Test
    public void test1() {

        String in = "{ \"field\" : \"hello!\" }";
        Entity entity = readValue(in, Entity.class);
        String out = writeValueAsString(entity);

        Assert.assertEquals(in, out);
    }

    public static class Entity {

        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

}