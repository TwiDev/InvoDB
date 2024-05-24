package ch.twidev.invodb.common.query.operations.search;

import ch.twidev.invodb.common.query.FieldMap;

import java.util.Arrays;

public class SearchFilter {

    private final FieldMap searchMap = new FieldMap();

    public static SearchFilter all() {
        return new SearchFilter(SearchFilterType.ALL, searchField -> true);
    }

    public static SearchFilter or(SearchFilter... operations) {
        return new SubSearchFilter(SearchFilterType.OR, searchField ->
                Arrays.stream(operations)
                        .anyMatch(operation -> operation.getSearchCondition().evaluate(searchField))
        , operations);
    }

    public static SearchFilter and(SearchFilter... operations) {
        return new SubSearchFilter(SearchFilterType.AND, searchField ->
            Arrays.stream(operations)
                    .allMatch(operation -> operation.getSearchCondition().evaluate(searchField))
        , operations);
    }

    public static SearchFilter eq(String value, Object object) {
        return new ObjectSearchFilter(value, object, SearchFilterType.EQUAL, searchField -> searchField.get(value).equals(object));
    }

    public static SearchFilter not_eq(String value, Object object) {
        return new ObjectSearchFilter(value, object, SearchFilterType.NOT_EQUAL, searchField -> !searchField.get(value).equals(object));
    }

    private final SearchCondition searchCondition;
    private final SearchFilterType searchFilterType;

    public SearchFilter(SearchFilterType searchFilterType, SearchCondition searchCondition) {
        this.searchCondition = searchCondition;
        this.searchFilterType = searchFilterType;
    }

    public SearchFilterType getSearchFilterType() {
        return searchFilterType;
    }

    public SearchCondition getSearchCondition() {
        return searchCondition;
    }

    public FieldMap getSearchMap() {
        return searchMap;
    }
}
