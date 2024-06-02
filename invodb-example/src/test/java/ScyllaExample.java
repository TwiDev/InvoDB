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

import java.io.File;
import java.net.InetSocketAddress;
import java.sql.Time;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScyllaExample {

    @Test
    public void run() throws DriverConnectionException {

        DriverConfig driverConfig = new ScyllaConfigBuilder()
                .setDriverName("ScyllaDriver")
                .addContactPoint(new ContactPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                .setAuthProvider(new PlainTextAuth("cassandra", "cassandra"))
                .build();

        try(ScyllaCluster scyllaDriver = new ScyllaCluster(driverConfig)) {
            scyllaDriver.asyncConnectSession("main").thenAccept(scyllaConnection -> {
                System.out.println("Current Thread (2) : " +Thread.currentThread().getName());
                InvoQuery.find("users")
                        .attribute("email")
                        .runAsync(scyllaConnection, (resultSet, t) -> {
                            if (t == null) {
                                while (resultSet.hasNext()) {
                                    Elements elements = resultSet.next();

                                    System.out.println(
                                            elements.getObject("email", String.class)
                                    );
                                }

                                System.out.println("Current Thread (3): " +Thread.currentThread().getName());
                            } else {
                                t.printStackTrace();
                            }
                        });
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            }).join();

            System.out.println("Current Thread (1) : " +Thread.currentThread().getName());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
