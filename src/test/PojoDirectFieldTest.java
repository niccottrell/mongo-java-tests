import codec.DateAsStringCodec;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ConventionDirectFieldImpl;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class PojoDirectFieldTest extends PojoTest {

    @Test
    public void test1() {

        final PojoCodecProvider provider =
                PojoCodecProvider.builder()
                        .automatic(true)
                        .conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION,
                                Conventions.CLASS_AND_PROPERTY_CONVENTION,
                                new ConventionDirectFieldImpl())) // this will make direct access possible
                        .build();

        final CodecRegistry registry = fromRegistries(
                MongoClient.getDefaultCodecRegistry(),
                fromProviders(provider)
        );

        String in = "{ \"field\" : \"hello!\" }";
        Entity entity = readValue(in, Entity.class, registry);
        System.out.println(entity);

        String out = writeValueAsString(entity, registry);

        Assert.assertEquals(in, out);
    }

    public static class Entity {

        private String field;

        public String getField() {
            throw new UnsupportedOperationException("You can't set the field here");
        }

        public void setField(String field) {
            throw new UnsupportedOperationException("You can't set the field here");
        }

        public String getValue() {
            return field;
        }

        public void setValue(String field) {
            this.field = field;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "field='" + field + '\'' +
                    '}';
        }
    }

}