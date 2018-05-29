import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class PojoProtectedGetterTest extends PojoTest {

    @Test
    public void test1() {

        String in = "{ \"protected\" : \"hello!\"}";
        Entity entity = readValue(in, Entity.class);
        String out = writeValueAsString(entity);

        assertThat(in, equalToIgnoringCase(out));
    }

    public static class Entity {

        private String prot;

        /**
         * Note: protected
         */
        protected String getProtected() {
            return prot;
        }

        public void setProtected(String prot) {
            this.prot = prot;
        }

    }

}