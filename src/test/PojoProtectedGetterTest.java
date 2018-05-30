import org.bson.codecs.pojo.annotations.BsonProperty;
import org.junit.Assert;
import org.junit.Test;

public class PojoProtectedGetterTest extends PojoTest {

    @Test
    public void test1() {

        String in = "{ \"protected\" : \"hello!\" }";
        Entity entity = readValue(in, Entity.class);
        String out = writeValueAsString(entity);

        Assert.assertEquals(in, out);
    }

    public static class Entity {

        private String prot;

        /**
         * Note: protected
         */
        @BsonProperty("protected")
        protected String getProtected() {
            return prot;
        }

        public void setProtected(String prot) {
            this.prot = prot;
        }

    }

}