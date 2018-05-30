package af;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;


import javax.print.Doc;
import java.util.Arrays;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.slice;

public class TwoPhaseCommitExample {

    public static void main(String[] args) {

        // no "ts" field to indicate available
        Document docA = new Document()
                .append("_id", "A")
                .append("history",
                        Arrays.asList(
                                new Document("state", 1),
                                new Document("state", 3)
                        ));

        // no "ts" field to indicate available
        Document docB = new Document()
                .append("_id", "B")
                .append("history",
                        Arrays.asList(
                                new Document("state", 3),
                                new Document("state", 5)
                        ));

        Document docC = new Document()
                .append("_id", "C")
                .append("ts", "2018-05-30") // this document is 'taken' already but a thread for processing
                .append("history",
                        Arrays.asList(
                                new Document("state", 2),
                                new Document("state", 5)
                        ));


        // link our codec registry to this client connection
        MongoClient mongoClient = new MongoClient("localhost");
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("2fc");

        // make the collection contain just the three documents above
        collection.drop();
        collection.insertMany(Arrays.asList(docA, docB, docC));

        int targetState = 5;

        Document candidate = (Document) collection.aggregate(Arrays.asList(
                // only consider available matches (also filter where the document has AT LEAST one history with the desired state value)
                Aggregates.match(fields(
                        new Document("history.state", targetState),
                        Filters.exists("ts", false))),
                // project just the last field of history array (see limit: -1)
                Aggregates.project(fields(new Document("lastHistory", new Document("$slice", Arrays.asList("$history", -1))))),
                // only where last last size is
                Aggregates.match(new Document("lastHistory.state", targetState))
        )).first();

        // at this point, it is possible that another field just grabbed this document, so we need to check
        Document query = new Document("_id", candidate.get("_id"))
                .append("ts", new Document("$exists", false));
        // claim this document by setting "ts" field
        Bson update = Updates.set("ts", "2018-05-30");

        Document res = collection.findOneAndUpdate(query, update);
        if (res != null) {
            // this is ours to work on
            System.out.println("We got a lock on: " + res);
            // TODO: Do stuff
            // Now that we've finished, unset the field
            Bson unset = Updates.unset("ts");
            collection.updateOne(new Document("_id", candidate.get("_id")), unset);
        } else {
            // another thread got there first
            // TODO: fetch another candidate
        }

    }

}
