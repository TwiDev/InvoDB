import ch.twidev.invodb.bridge.contexts.SearchFilterType;

import java.util.HashMap;
import java.util.logging.Logger;

public class ConnectionTest {

    private Logger logger = Logger.getLogger("yo");

    private final HashMap<SearchFilterType, String> CQL_OPERATORS = new HashMap<>(){{
        put(SearchFilterType.ALL, "*");
        put(SearchFilterType.AND, "and");
        put(SearchFilterType.OR, "or");
        put(SearchFilterType.EQUAL, "=");
        put(SearchFilterType.NOT_EQUAL, "!=");
    }};



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



    }

}
