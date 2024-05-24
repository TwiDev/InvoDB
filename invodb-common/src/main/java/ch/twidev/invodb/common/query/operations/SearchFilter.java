package ch.twidev.invodb.common.query.operations;

import ch.twidev.invodb.common.query.FieldMap;

import java.util.Arrays;

public class SearchFilter {

    private final FieldMap searchMap = new FieldMap();

    public static SearchFilter all() {
        return new SearchFilter(searchField -> true);
    }

    public static SearchFilter or(SearchFilter... operations) {
        return new SearchFilter(searchField ->
                Arrays.stream(operations)
                        .anyMatch(operation -> operation.getSearchCondition().evaluate(searchField))
        );
    }

    public static SearchFilter and(SearchFilter... operations) {
        return new SearchFilter(searchField ->
            Arrays.stream(operations)
                    .allMatch(operation -> operation.getSearchCondition().evaluate(searchField))
        );
    }

    public static SearchFilter eq(String value, Object object) {
        return new SearchFilter(searchField -> searchField.get(value).equals(object));
    }

    public static SearchFilter not_eq(String value, Object object) {
        return new SearchFilter(searchField -> !searchField.get(value).equals(object));
    }

    private final SearchCondition searchCondition;

    public SearchFilter(SearchCondition searchCondition) {
        this.searchCondition = searchCondition;
    }

    public SearchCondition getSearchCondition() {
        return searchCondition;
    }

    public FieldMap getSearchMap() {
        return searchMap;
    }
}
