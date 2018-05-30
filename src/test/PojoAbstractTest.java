import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PojoAbstractTest extends PojoTest {

    @Test
    public void test1() {

        Entity entity1 = new Entity(new ObjectId());
        entity1.setValue("hello1");
        entity1.setBaseField("base1");

        Entity entity2 = new Entity(new ObjectId());
        entity2.setValue("hello2");
        entity2.setBaseField("base2");

        List<AbstractEntity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);

        // link our codec registry to this client connection
        MongoClientOptions clientOptions = MongoClientOptions.builder().codecRegistry(CODEC_REGISTRY).build();
        MongoClient mongoClient = new MongoClient("localhost", clientOptions);
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<AbstractEntity> collection = database.getCollection("pojos", AbstractEntity.class);

        collection.insertMany(entities);

    }

    @BsonDiscriminator
    public abstract class AbstractEntity {

        String baseField = "base!";

        public String getBaseField() {
            return baseField;
        }

        public void setBaseField(String baseField) {
            this.baseField = baseField;
        }

        public abstract ObjectId getId();

    }

    public class Entity extends AbstractEntity {

        private final ObjectId id;

        private String value;

        @BsonCreator
        public Entity(@BsonProperty("id") ObjectId id) {
            this.id = id;
        }

        public ObjectId getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}