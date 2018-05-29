import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class PojoPrivateFieldPublicMethodsTest extends PojoTest {

    @Test
    public void test1() {

        String in = "{ \"field\" : \"hello!\"}";
        Entity entity = readValue(in, Entity.class);
        String out = writeValueAsString(entity);

        assertThat(in, equalToIgnoringCase(out));
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