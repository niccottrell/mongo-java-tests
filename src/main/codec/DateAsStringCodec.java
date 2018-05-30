package codec;

import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;

public class DateAsStringCodec
        implements Codec<Date> {

    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    public DateFormat getDF() {
        return DF;
    }

    public void encode(
            BsonWriter writer,
            Date value,
            EncoderContext encoderContext) {

        writer.writeString(getDF().format(value));
    }

    public Date decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        String value = reader.readString();
        try {
            return getDF().parse(value);
        } catch (ParseException e) {
            throw new BsonInvalidOperationException(format(
                    "Could not parse value as java.util.Date: %s",value), e);
        }
    }

    public Class<Date> getEncoderClass() {
        return Date.class;
    }
}