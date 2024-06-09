package ch.twidev.invodb.driver.mongodb.filter;

import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.operations.SearchContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
import ch.twidev.invodb.bridge.search.ICompositeSearchFilter;
import ch.twidev.invodb.bridge.search.IFieldSearchFilter;
import ch.twidev.invodb.bridge.search.ISearchFilter;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;

public class BsonFilter {

    public static Bson toBson(SearchContext searchContext, PlaceholderContext placeholderContext) {
        return toBson(searchContext.getSearchFilter(), placeholderContext);
    }

    public static Bson toBson(ISearchFilter searchFilter, PlaceholderContext placeholderContext){
        if(searchFilter == null || searchFilter.getSearchFilterType() == SearchFilterType.ALL) return null;

        if(searchFilter instanceof ICompositeSearchFilter compositeSearchFilter) {
            final Bson[] children = compositeSearchFilter.getSearchFilter()
                    .stream()
                    .map(filter -> toBson(searchFilter, placeholderContext))
                    .toArray(Bson[]::new);

            return switch (searchFilter.getSearchFilterType()) {
                case AND -> and(children);
                case OR -> or(children);

                default -> throw new IllegalStateException("Unexpected value: " + searchFilter.getSearchFilterType());
            };
        }

        if(searchFilter instanceof IFieldSearchFilter fieldSearchFilter) {
            Object object = fieldSearchFilter.getObject();

            if(object instanceof QueryPlaceholder queryPlaceholder) {
                object = placeholderContext.get(queryPlaceholder);
            }

            return switch (searchFilter.getSearchFilterType()) {
                case EQUAL -> eq(fieldSearchFilter.getValue(), object);
                case NOT_EQUAL -> not(eq(fieldSearchFilter.getValue(), object));

                default -> throw new IllegalStateException("Unexpected value: " + searchFilter.getSearchFilterType());
            };
        }

        return null;
    }
}
