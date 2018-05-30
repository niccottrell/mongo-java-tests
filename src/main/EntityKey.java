import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.Objects;

public class EntityKey {

    final String code;

    final Date date;

    @BsonCreator
    public EntityKey(@BsonProperty("code") String code, @BsonProperty("date") Date date) {
        this.code = code;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityKey entityKey = (EntityKey) o;
        return Objects.equals(code, entityKey.code) &&
                Objects.equals(date, entityKey.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code, date);
    }
}
