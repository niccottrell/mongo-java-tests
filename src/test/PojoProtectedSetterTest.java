import org.junit.Assert;
import org.junit.Test;

public class PojoProtectedSetterTest extends PojoTest {

    @Test
    public void test1() {

        String in = "{ \"protected\" : \"hello!\" }";
        Entity entity = readValue(in, Entity.class);
        String out = writeValueAsString(entity);

        Assert.assertEquals(in, out);
    }

    public static class Entity {

        private String prot;

        public String getProtected() {
            return prot;
        }

        /**
         * Note: protected BUT Convention allows setting via reflection
         */
        protected void setProtected(String prot) {
            this.prot = prot;
        }
    }

}