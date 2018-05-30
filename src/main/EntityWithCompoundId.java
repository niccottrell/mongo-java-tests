import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

public class EntityWithCompoundId {

    @BsonId
    private final EntityKey key;

    private String value;

    @BsonCreator
    public EntityWithCompoundId(@BsonProperty("key") EntityKey key) {
        this.key = key;
    }

    public EntityKey getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityWithCompoundId that = (EntityWithCompoundId) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);
    }
}
