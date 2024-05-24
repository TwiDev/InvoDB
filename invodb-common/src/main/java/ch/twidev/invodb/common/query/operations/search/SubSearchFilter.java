package ch.twidev.invodb.common.query.operations.search;

import java.util.List;

public class SubSearchFilter extends SearchFilter{

    private final List<SearchFilter> searchFilters;

    public SubSearchFilter(SearchFilterType searchFilterType, SearchCondition searchCondition, SearchFilter... searchFilters) {
        super(searchFilterType, searchCondition);

        this.searchFilters = List.of(searchFilters);
    }

    public List<SearchFilter> getSearchFilters() {
        return searchFilters;
    }
}
