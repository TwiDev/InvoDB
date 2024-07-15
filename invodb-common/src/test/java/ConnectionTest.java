import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
import ch.twidev.invodb.common.cache.StreamCacheProvider;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
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


    public static SearchDictionary searchDictionary = new SearchDictionary(){{
        put(SearchFilterType.ALL, new SearchCompositeParameter("*"));
        put(SearchFilterType.AND, new SearchCompositeParameter("AND"));
        put(SearchFilterType.OR, new SearchCompositeParameter("OR"));
        put(SearchFilterType.EQUAL, new SearchFieldParameter() {
            @Override
            public String parse(String key, List<Object> context) {
                return key + " = ?";
            }
        });
        put(SearchFilterType.NOT_EQUAL, new SearchFieldParameter() {
            @Override
            public String parse(String key, List<Object> context) {
                return key + " != ?";
            }
        });
        put(SearchFilterType.IN, new SearchFieldParameter() {
            @Override
            public String parse(String key, List<Object> context) {
                String sub = ("?,".repeat(context.size()));
                return key + " in(" + sub.substring(0, sub.length() - 1) + ")";
            }
        });
    }};


    @Test
    public void testFilter() {

        SearchFilter searchFilter = and(in("test", 2,4,6,8,110), eq("name", "Hello"));
        System.out.println(searchFilter.toQuery(searchDictionary, null));
        System.out.println(searchFilter.getContexts());
    }


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
