import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
import ch.twidev.invodb.common.cache.StreamCacheProvider;
import ch.twidev.invodb.common.query.InvoQuery;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.logging.Logger;

import static ch.twidev.invodb.common.query.operations.search.SearchFilter.eq;

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
    public void testQueryCache() throws InterruptedException {
      /*  InvoQuery<ElementSet> invoQuery = InvoQuery.find("users")
                .where(eq("name", QueryCachePlaceHolder.USER_NAME))
                .attribute("email")
                .attribute("power")
                .setPlaceholder(PlaceholderContext.from(QueryCachePlaceHolder.USER_NAME, "TwyDev"));

        logger.info("HashCode of Query (1): " + invoQuery.hashCode());

        InvoQuery<ElementSet> invoQuery2 = InvoQuery.find("users")
                .where(eq("name", QueryCachePlaceHolder.USER_NAME))
                .attribute("email")
                .attribute("power")
                .setPlaceholder(PlaceholderContext.from(QueryCachePlaceHolder.USER_NAME, "TwyDev"));

        System.out.println("Is Equal? = " + (invoQuery.equals(invoQuery2)));

        Cache<String, Object> cache = new StreamCacheProvider<>(
                null,
                CachingStrategy.LRU,
                "testCache",
                1000);


        logger.info("HashCode of Query (2): " + invoQuery2.hashCode());*/
    }

    enum QueryCachePlaceHolder implements QueryPlaceholder {
        USER_NAME("userName");

        private final String placeholder;

        QueryCachePlaceHolder(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        public String getPlaceholder() {
            return placeholder;
        }
    }

}
