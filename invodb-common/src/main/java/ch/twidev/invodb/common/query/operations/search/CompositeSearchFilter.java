package ch.twidev.invodb.common.query.operations.search;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.search.SearchCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeSearchFilter extends SearchFilter{

    private final List<SearchFilter> searchFilters;

    public CompositeSearchFilter(SearchFilterType searchFilterType, SearchCondition searchCondition, SearchFilter... searchFilters) {
        super(searchFilterType, searchCondition);

        this.searchFilters = List.of(searchFilters);
    }

    public List<SearchFilter> getSearchFilters() {
        return searchFilters;
    }

    @Override
    public String toQuery(SearchDictionary searchDictionary, PlaceholderContext placeholderContext) {
        String joinedFilters = searchFilters.stream()
                .map(searchFilter -> searchFilter.toQuery(searchDictionary, placeholderContext))
                .collect(Collectors.joining(" " + searchDictionary.getComposite(this.getSearchFilterType()).value() + " "));

        return "(" + joinedFilters + ")";
    }

    @Override
    public List<Object> getContexts() {
        return searchFilters.stream().map(SearchFilter::getContexts).collect(Collectors.toList());
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
