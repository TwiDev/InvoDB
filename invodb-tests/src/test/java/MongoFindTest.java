import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.driver.mongodb.MongoCluster;
import ch.twidev.invodb.driver.mongodb.MongoConnection;
import ch.twidev.invodb.driver.mongodb.URLMongoConfigBuilder;

import static ch.twidev.invodb.common.query.operations.search.SearchFilter.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MongoFindTest {

    @Test
    public void find() {
        try {
            try (MongoConnection connection = new MongoCluster(new URLMongoConfigBuilder("mongodb://localhost:27017/").build()).connectSession("main")) {
                CompletableFuture<ElementSet> elementSet = InvoQuery.find("users")
                        .where(or(eq("name", "TwyDev"),eq("name","test")))
                        .runAsync(connection);

                elementSet.thenAccept(set -> {
                    System.out.println(set.isEmpty());

                    while (set.hasNext()) {
                        Elements elements = set.next();

                        System.out.println(elements.getObject("email"));
                    }
                }).join();
            }
        } catch (DriverConnectionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
