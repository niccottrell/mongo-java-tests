import com.mongodb.MongoClient;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.json.JsonReader;
import org.bson.json.JsonWriter;

import java.io.StringWriter;
import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class PojoTest {

    static final PojoCodecProvider POJO_PROVIDER =
            PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION,
                            Conventions.CLASS_AND_PROPERTY_CONVENTION, Conventions.SET_PRIVATE_FIELDS_CONVENTION))
                    .build();

    static final CodecRegistry CODEC_REGISTRY = fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            fromProviders(POJO_PROVIDER)
    );

    protected <T> T readValue(String json, Class<T> entityClass) {
        return readValue(json, entityClass, CODEC_REGISTRY);
    }

    protected <T> T readValue(String json, Class<T> entityClass, CodecRegistry registry) {
        // get the codec for this class
        Codec<T> codec = registry.get(entityClass);
        // prepare a context
        DecoderContext context = DecoderContext.builder().build();
        // decode into a new object
        T result = codec.decode(new JsonReader(json), context);
        return result;
    }

    protected <T> String writeValueAsString(T dummy) {
        return writeValueAsString(dummy, CODEC_REGISTRY);
    }

    protected <T> String writeValueAsString(T dummy, CodecRegistry registry) {
        // get the codec for this class
        Codec<T> codec = registry.get((Class<T>) dummy.getClass());
        // prepare a context
        EncoderContext context = EncoderContext.builder().build();
        // prepare output buffer
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        codec.encode(jsonWriter, dummy, context);
        return stringWriter.toString();
    }

}
