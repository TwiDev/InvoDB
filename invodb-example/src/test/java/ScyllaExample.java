import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;

import static ch.twidev.invodb.common.query.operations.search.SearchFilter.*;

public class ScyllaExample {

    public void run() {

        try (ScyllaCluster scyllaDriver = new ScyllaCluster(null)) {
            try (ScyllaConnection scyllaConnection = scyllaDriver.connectSession("test")) {

                InvoQuery.find("main")
                        .where(and(
                                eq("user_name", "TwiDev"),
                                or(
                                        not_eq("user_id", 2),
                                        not_eq("user_id", 3)
                                )))
                        .attribute("user_email")
                        .attribute("user_age")
                        .run(scyllaConnection, (resultSet, throwable) -> {
                            if (throwable != null) {
                                while (resultSet.hasNext()) {
                                    Elements elements = resultSet.next();

                                    System.out.println(
                                            elements.getObject("email", String.class)
                                    );
                                }
                            } else {
                                throwable.printStackTrace();
                            }
                        });
            }
        }
    }

}
