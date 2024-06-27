import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
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
                CompletableFuture<ElementSet<?>> elementSet = InvoQuery.find("users")
                        .where(or(eq("name", "TwyDev"),eq("name","test")))
                        .runAsync(connection);

                elementSet.thenAccept(set -> {
                    System.out.println(set.isEmpty());

                    while (set.hasNext()) {
                        Elements elements = set.next();

                        System.out.println(elements.getObject("email"));
                    }
                }).join();

                InvoQuery.update("users")
                        .where(eq("name","test"))
                        .field("email", MongoPlaceHolder.EMAIL)
                        .runAsync(connection, PlaceholderContext.from(MongoPlaceHolder.EMAIL, "helloworld@gmail.com"))
                        .thenAccept(operationResult -> {
                            System.out.println(operationResult.isOk());
                        })
                        .join();

                InvoQuery.insert("users")
                        .field("name", "Hello")
                        .field("email", "world@gmail.com")
                        .runAsync(connection)
                        .thenAccept(operationResult -> {
                            System.out.println("Inserted");
                        })
                        .join();
            }
        } catch (DriverConnectionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum MongoPlaceHolder implements QueryPlaceholder {

        EMAIL("email");

        private final String email;

        MongoPlaceHolder(String email) {
            this.email = email;
        }

        @Override
        public String getPlaceholder() {
            return email;
        }
    }

}
