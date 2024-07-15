package ch.twidev.invodb.driver.scylla.filter;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.search.ISearchFilter;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.ListUI;
import java.util.List;

public class ScyllaFilter {

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


    public static String toQuery(ISearchFilter iSearchFilter, PlaceholderContext placeholderContext) {
        return null;
    }

}
