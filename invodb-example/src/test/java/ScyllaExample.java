import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScyllaExample {

    @Test
    public void run() throws DriverConfigException {

        DriverConfig driverConfig = new ScyllaConfigBuilder()
                .setDriverName("ScyllaDriver")
                .addContactPoint(new ContactPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                .setAuthProvider(new PlainTextAuth("cassandra","cassandra"))
                .build();

        try (ScyllaCluster scyllaDriver = new ScyllaCluster(driverConfig)) {
            scyllaDriver.asyncConnectSession("main", (scyllaConnection, e) -> {
                    InvoQuery.find("users")
                            .attribute("email")
                            .run(scyllaConnection, (resultSet, throwable) -> {
                                if (throwable == null) {
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
            });

        } catch (DriverConnectionException e) {
            throw new RuntimeException(e);
        }
    }

}
