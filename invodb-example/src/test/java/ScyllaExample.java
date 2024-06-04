import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ch.twidev.invodb.common.query.operations.search.SearchFilter.*;

public class ScyllaExample {

    private static final Logger logger = Logger.getLogger("ScyllaExample");

    @Test
    public void run() throws DriverConnectionException {

        DriverConfig driverConfig = new ScyllaConfigBuilder()
                .setDriverName("ScyllaDriver")
                .addContactPoint(new ContactPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                .setAuthProvider(new PlainTextAuth("cassandra", "cassandra"))
                .build();

        try(ScyllaCluster scyllaDriver = new ScyllaCluster(driverConfig)) {
            logger.info("[Main] Current Thread: " +Thread.currentThread().getName());

            scyllaDriver.connectSessionAsync("main").thenAccept(scyllaConnection -> {
                logger.info("[Async] Before waiting Thread: " +Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                logger.info("[Async] After waiting Thread: " +Thread.currentThread().getName());
                InvoQuery.find("users")
                        .attribute("email")
                        .runAsync(scyllaConnection, (resultSet, t) -> {
                            if (t == null) {
                                while (resultSet.hasNext()) {
                                    Elements elements = resultSet.next();

                                    logger.info("[Async] Response: " +
                                            elements.getObject("email", String.class)
                                    );
                                }

                                logger.info("[Async] Current Thread: " +Thread.currentThread().getName());
                            } else {
                                t.printStackTrace();
                            }
                        });
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            });

            InvoQuery.find("users")
                    .attribute("email")
                    .where(
                            eq("name", UserPlaceHolder.USER_NAME)
                    )
                    .setPlaceholder(
                            PlaceholderContext.empty()
                                    .set(UserPlaceHolder.USER_NAME, "TwiDev")
                    )
                    .run(scyllaDriver.connectSession("main"), (resultSet, t) -> {
                        if (t == null) {
                            if(resultSet.isEmpty()) {
                                logger.severe("[Sync] Cannot be found !");
                                return;
                            }

                            while (resultSet.hasNext()) {
                                Elements elements = resultSet.next();

                                logger.info("[Sync] Response: " +
                                        elements.getObject("email", String.class)
                                );
                            }

                            logger.info("[Sync] Current Thread: " +Thread.currentThread().getName());
                        } else {
                            t.printStackTrace();
                        }
                    });

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public enum UserPlaceHolder implements QueryPlaceholder {

        USER_NAME("name");

        private final String name;

        UserPlaceHolder(String name) {
            this.name = name;
        }

        @Override
        public String getPlaceholder() {
            return name;
        }
    }

}
