package ch.twidev.invodb.common.query.operations.search;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
import ch.twidev.invodb.bridge.search.IFieldSearchFilter;
import ch.twidev.invodb.bridge.search.SearchCondition;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FieldSearchFilter extends SearchFilter implements IFieldSearchFilter {

    private final String value;
    private final List<Object> object;

    public FieldSearchFilter(String value, SearchFilterType searchFilterType, SearchCondition searchCondition, Object... object) {
        super(searchFilterType, searchCondition);
        this.value = value;
        this.object = Arrays.asList(object);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public String toQuery(SearchDictionary searchDictionary, PlaceholderContext placeholderContext) {
        for (int i = 0; i < object.size(); i++) {
            Object o = object.get(i);

            if(o instanceof QueryPlaceholder queryPlaceholder && placeholderContext != null) {
                o = placeholderContext.get(queryPlaceholder);
            }

            object.set(i, o);

        }
        return searchDictionary.getField(this.getSearchFilterType())
                .parse(value, object);
    }

    @Override
    public int getTotalHashCode() {
        return value.hashCode();
    }

    @Override
    public List<Object> getContexts() {
        return object;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
