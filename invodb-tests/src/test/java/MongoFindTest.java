import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.driver.mongodb.MongoCluster;
import ch.twidev.invodb.driver.mongodb.MongoConnection;
import ch.twidev.invodb.driver.mongodb.URLMongoConfigBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MongoFindTest {

    @Test
    public void find() {
        try {
            try (MongoConnection connection = new MongoCluster(new URLMongoConfigBuilder("dd").build()).connectSession("main")) {
                ElementSet elementSet = InvoQuery.find("test")
                        .where(SearchFilter.eq("name", "TwyDev"))
                        .run(connection);

                while (elementSet.hasNext()) {
                    Elements elements = elementSet.next();

                    System.out.println(elements.getObject("email"));
                }
            }
        } catch (DriverConnectionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
