package ch.twidev.invodb.driver.mongodb.filter;

import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.operations.SearchContext;
import ch.twidev.invodb.bridge.search.ICompositeSearchFilter;
import ch.twidev.invodb.bridge.search.IFieldSearchFilter;
import ch.twidev.invodb.bridge.search.ISearchFilter;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;

public class BsonFilter {

    public static Bson toBson(SearchContext searchContext) {
        return toBson(searchContext.getSearchFilter());
    }

    public static Bson toBson(ISearchFilter searchFilter){
        if(searchFilter == null || searchFilter.getSearchFilterType() == SearchFilterType.ALL) return null;

        if(searchFilter instanceof ICompositeSearchFilter compositeSearchFilter) {
            final Bson[] children = compositeSearchFilter.getSearchFilter()
                    .stream()
                    .map(BsonFilter::toBson)
                    .toArray(Bson[]::new);

            return switch (searchFilter.getSearchFilterType()) {
                case AND -> and(children);
                case OR -> or(children);

                default -> throw new IllegalStateException("Unexpected value: " + searchFilter.getSearchFilterType());
            };
        }

        if(searchFilter instanceof IFieldSearchFilter fieldSearchFilter) {
            return switch (searchFilter.getSearchFilterType()) {
                case EQUAL -> eq(fieldSearchFilter.getValue(), fieldSearchFilter.getObject());
                case NOT_EQUAL -> not(eq(fieldSearchFilter.getValue(), fieldSearchFilter.getObject()));

                default -> throw new IllegalStateException("Unexpected value: " + searchFilter.getSearchFilterType());
            };
        }

        return null;
    }
}
