import org.junit.Assert;
import org.junit.Test;

public class PojoFieldnameMismatchTest extends PojoTest {

    @Test
    public void test1() {

        String in = "{ \"private\" : \"hello!\" }";
        Entity entity = readValue(in, Entity.class);
        String out = writeValueAsString(entity);

        Assert.assertEquals(in, out);
    }

    public static class Entity {

        private String priv;

        public String getPrivate() {
            return priv;
        }

        public void setPrivate(String priv) {
            this.priv = priv;
        }
    }

}