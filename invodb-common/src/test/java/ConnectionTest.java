import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.SearchFilter;
import ch.twidev.invodb.common.query.operations.SearchOperation;
import org.junit.Test;

import static ch.twidev.invodb.common.query.operations.SearchFilter.*;

import java.util.logging.Logger;

public class ConnectionTest {

    private Logger logger = Logger.getLogger("yo");

    @Test
    public void testScyllaDriverConnection() throws InterruptedException {

    /*    try {
            ScyllaClusterDriver invoClusterDriver = new ScyllaClusterDriver(new DriverConfig.ScyllaBuilder()
                    .setDriverName("Test")
                    .addContactPoint(new ClusterPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                    .setAuthenticator(new Authenticator("cassandra","cassandra"))
                    .build());

            invoClusterDriver.initConnection();
            invoClusterDriver.asyncConnect("main").thenAccept(scyllaSession -> {
                        System.out.println("HELLO");
                        logger.info("HELLO");
                        scyllaSession.execute("SELECT * FROM users");
                    });

        } catch (DriverBuilderException | DriverConnectionException e) {
            throw new RuntimeException(e);
        }*/

        InvoQuery.find("main")
                .where(and(
                        eq("user_name", "TwiDev"),
                        not_eq("user_id", 2)))
                .run(null /*driver*/, (resultSet, throwable) -> {

                });

    }

}
