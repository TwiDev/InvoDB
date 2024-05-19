import ch.twidev.invodb.authentication.Authenticator;
import ch.twidev.invodb.cluster.ClusterPoint;
import ch.twidev.invodb.drivers.DriverConfig;
import ch.twidev.invodb.drivers.driver.InvoClusterDriver;
import ch.twidev.invodb.exceptions.DriverBuilderException;
import ch.twidev.invodb.exceptions.DriverConnectionException;
import org.junit.Test;

import java.net.InetSocketAddress;

public class ConnectionTest {

    @Test
    public void testScyllaDriverConnection() {

        try {
            InvoClusterDriver invoClusterDriver = new InvoClusterDriver(new DriverConfig.ScyllaBuilder()
                    .setDriverName("Test")
                    .addContactPoint(new ClusterPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                    .setKeyspace("main")
                    .setAuthenticator(new Authenticator("cassandra","cassandra"))
                    .build());

            invoClusterDriver.initConnection();
            invoClusterDriver.asyncConnect("main")
                    .exceptionally(throwable -> {
                        System.out.println("Error : " + throwable);
                        return null;
                    }).thenAcceptAsync(invoSessionDriver -> {
                        System.out.println("Driver : " + invoSessionDriver);
                    });
        } catch (DriverBuilderException | DriverConnectionException e) {
            throw new RuntimeException(e);
        }

    }

}
