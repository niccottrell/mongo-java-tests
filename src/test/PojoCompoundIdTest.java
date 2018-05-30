import codec.DateAsStringCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

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
        EntityWithCompoundId entity = new EntityWithCompoundId(key);
        entity.setValue("hello!");

        String out = writeValueAsString(entity, registry);
        Assert.assertEquals(in, out);

        EntityWithCompoundId entity2 = readValue(in, EntityWithCompoundId.class, registry);
        Assert.assertEquals(entity, entity2);

    }


}