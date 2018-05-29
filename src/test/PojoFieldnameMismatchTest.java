import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.is;


public class PojoFieldnameMismatchTest extends PojoTest {

    @Test
    public void test1() {

        String in = "{\"private\" : \"hello!\"}";
        Entity entity = readValue(in, Entity.class);
        String out = writeValueAsString(entity);

        assertThat(in, is(equalToIgnoringWhiteSpace(out)));
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