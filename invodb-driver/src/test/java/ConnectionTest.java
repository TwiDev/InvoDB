import ch.twidev.invodb.authentication.Authenticator;
import ch.twidev.invodb.cluster.ClusterPoint;
import ch.twidev.invodb.drivers.DriverConfig;
import ch.twidev.invodb.drivers.driver.ScyllaClusterDriver;
import ch.twidev.invodb.exceptions.DriverBuilderException;
import ch.twidev.invodb.exceptions.DriverConnectionException;
import org.junit.Test;

import java.net.InetSocketAddress;

public class ConnectionTest {

    @Test
    public void testScyllaDriverConnection() throws InterruptedException {

        try {
            ScyllaClusterDriver invoClusterDriver = new ScyllaClusterDriver(new DriverConfig.ScyllaBuilder()
                    .setDriverName("Test")
                    .addContactPoint(new ClusterPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                    .setAuthenticator(new Authenticator("cassandra","cassandra"))
                    .build());

            invoClusterDriver.initConnection();
            invoClusterDriver.asyncConnect("main").
                    exceptionally(throwable -> {
                        System.out.println("ERROR");
                        throwable.printStackTrace();
                        return null;
                    }).thenAccept(scyllaSession -> {
                        System.out.println("HELLO");
                        scyllaSession.execute("SELECT * FROM users");
                    });

        } catch (DriverBuilderException | DriverConnectionException e) {
            throw new RuntimeException(e);
        }


    }

}
