package ch.twidev.invodb.common.query.operations.search;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
import ch.twidev.invodb.bridge.search.IFieldSearchFilter;
import ch.twidev.invodb.bridge.search.SearchCondition;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;

import java.util.Collections;
import java.util.List;

public class FieldSearchFilter extends SearchFilter implements IFieldSearchFilter {

    private final String value;
    private Object object;

    public FieldSearchFilter(String value, Object object, SearchFilterType searchFilterType, SearchCondition searchCondition) {
        super(searchFilterType, searchCondition);
        this.value = value;
        this.object = object;
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
        if(object instanceof QueryPlaceholder queryPlaceholder && placeholderContext != null) {
            this.object = placeholderContext.get(queryPlaceholder);
        }

        return searchDictionary.getField(this.getSearchFilterType())
                .parse(value);
    }

    @Override
    public int getTotalHashCode() {
        return value.hashCode();
    }

    @Override
    public List<Object> getContexts() {
        return Collections.singletonList(object);
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
