import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.*;

import java.util.HashMap;
import java.util.logging.Logger;

import static ch.twidev.invodb.common.query.operations.search.SearchFilter.*;

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

        InvoQuery.find("main")
                .where(and(
                        eq("user_name", "TwiDev"),
                        not_eq("user_id", 2)))
                .attribute("user_email")
                .run(null, (resultSet, throwable) -> {
                    if(throwable != null) {

                    }
                });


        SearchDictionary searchDictionary = new SearchDictionary(){{
            put(SearchFilterType.ALL, new SearchCompositeParameter("*"));
            put(SearchFilterType.AND, new SearchCompositeParameter("AND"));
            put(SearchFilterType.OR, new SearchCompositeParameter("OR"));
            put(SearchFilterType.EQUAL, new SearchFieldParameter() {
                @Override
                public String parse(String key) {
                    return key + " = ?";
                }
            });
            put(SearchFilterType.NOT_EQUAL, new SearchFieldParameter() {
                @Override
                public String parse(String key) {
                    return key + " = ?";
                }
            });
        }};

        SearchFilter filter = and(
                SearchFilter.eq("name", "John"),
                SearchFilter.or(
                        SearchFilter.eq("age", 30),
                        SearchFilter.not_eq("city", "New York")
                )
        );

        System.out.println(filter.toQuery(searchDictionary, null)); /* OUTPUT : (name = 'John' AND (age = '30' OR city = 'New York'))*/



    }

}
