import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.ObjectSearchFilter;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.common.query.operations.search.SearchFilterType;
import ch.twidev.invodb.common.query.operations.search.SubSearchFilter;
import org.junit.Test;

import static ch.twidev.invodb.common.query.operations.search.SearchFilter.*;

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

       /* InvoQuery.find("main")
                .where(and(
                        eq("user_name", "TwiDev"),
                        not_eq("user_id", 2)))
                .run(null  (resultSet, throwable) -> {

                });
        */

        System.out.println(
                prepareSearch(SearchFilter.and(SearchFilter.eq("user_name","TwiDev"), SearchFilter.or(SearchFilter.not_eq("user_id",2), SearchFilter.not_eq("user_id",3))))
        );

    }

    public String prepareSearch(SearchFilter searchFilter) {
        SearchFilterType searchFilterType = searchFilter.getSearchFilterType();
        if(!CQL_OPERATORS.containsKey(searchFilterType)) return "";

        String operator = CQL_OPERATORS.get(searchFilterType);
        if(searchFilter instanceof SubSearchFilter subSearchFilter) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < subSearchFilter.getSearchFilters().size(); i++) {
                stringBuilder.append(
                        prepareSearch(subSearchFilter.getSearchFilters().get(i))
                );

                if(i < subSearchFilter.getSearchFilters().size() - 1) {
                    stringBuilder.append(operator).append(" ");
                }
            }

            return stringBuilder.toString();
        }else if(searchFilter instanceof ObjectSearchFilter objectSearchFilter) {
            return "%s %s %s ".formatted(objectSearchFilter.getValue(), operator, objectSearchFilter.getObject()/*TODO: format string, number,...*/);
        }

        return "";
    }

}
