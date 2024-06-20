package ch.twidev.invodb.common.query.operations.search;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.search.ISearchFilter;
import ch.twidev.invodb.bridge.contexts.FieldMap;
import ch.twidev.invodb.bridge.search.SearchCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SearchFilter implements ISearchFilter {

    private final FieldMap searchMap = new FieldMap();

    private static final int prime = 53;

    public static SearchFilter all() {
        return new SearchFilter(SearchFilterType.ALL, searchField -> true) {
            @Override
            public String toQuery(SearchDictionary searchDictionary, PlaceholderContext placeholderContext) {
                return null;
            }

            @Override
            public int getTotalHashCode() {
                return prime;
            }

            @Override
            public List<Object> getContexts() {
                return new ArrayList<>();
            }
        };
    }

    public static SearchFilter or(SearchFilter... operations) {
        return new CompositeSearchFilter(SearchFilterType.OR, searchField -> Arrays.stream(operations)
                .anyMatch(operation -> operation.getSearchCondition().evaluate(searchField)), operations);

    }

    public static SearchFilter and(SearchFilter... operations) {
        return new CompositeSearchFilter(SearchFilterType.AND, searchField -> Arrays.stream(operations)
                .allMatch(operation -> operation.getSearchCondition().evaluate(searchField)), operations);
    }

    public static SearchFilter eq(String value, Object object) {
        return new FieldSearchFilter(value, object, SearchFilterType.EQUAL, searchField -> searchField.get(value).equals(object));
    }

    public static SearchFilter not_eq(String value, Object object) {
        return new FieldSearchFilter(value, object, SearchFilterType.NOT_EQUAL, searchField -> !searchField.get(value).equals(object));
    }

    private final SearchCondition searchCondition;
    private final SearchFilterType searchFilterType;

    public SearchFilter(SearchFilterType searchFilterType, SearchCondition searchCondition) {
        this.searchCondition = searchCondition;
        this.searchFilterType = searchFilterType;
    }

    @Override
    public SearchFilterType getSearchFilterType() {
        return searchFilterType;
    }

    @Override
    public SearchCondition getSearchCondition() {
        return searchCondition;
    }

    @Override
    public abstract String toQuery(SearchDictionary searchDictionary, PlaceholderContext queryPlaceholder);

    @Override
    public abstract int getTotalHashCode();


    public abstract List<Object> getContexts();

    @Override
    public boolean isRequired() {
        return searchFilterType != SearchFilterType.ALL;
    }
}
