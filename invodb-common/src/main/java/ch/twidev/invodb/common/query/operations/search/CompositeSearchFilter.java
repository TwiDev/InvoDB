package ch.twidev.invodb.common.query.operations.search;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.search.ICompositeSearchFilter;
import ch.twidev.invodb.bridge.search.IFieldSearchFilter;
import ch.twidev.invodb.bridge.search.ISearchFilter;
import ch.twidev.invodb.bridge.search.SearchCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositeSearchFilter extends SearchFilter implements ICompositeSearchFilter {

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
        return searchFilters.stream()
                .map(searchFilter -> searchFilter.toQuery(searchDictionary, placeholderContext))
                .collect(Collectors.joining(" " + searchDictionary.getComposite(this.getSearchFilterType()).value() + " "));
    }

    @Override
    public int getTotalHashCode() {
        int hash = 13 * this.getSearchFilterType().getQueryCode();

        for (SearchFilter searchFilter : searchFilters) {
            hash += searchFilter.getTotalHashCode();
        }

        return hash;
    }

    @Override
    public List<Object> getContexts() {
        //return searchFilters.stream().map(SearchFilter::getContexts::).collect(Collectors.toList());
        return searchFilters.stream().flatMap(filter -> filter.getContexts().stream()).collect(Collectors.toList());
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public List<ISearchFilter> getSearchFilter() {
        return new ArrayList<>(searchFilters);
    }
}
