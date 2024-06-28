package ch.twidev.invodb.common.query;

import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.query.operations.SearchOperation;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;

public abstract class SearchQuery<Result, Query> extends InvoQuery<Result> implements SearchOperation<Query> {

    protected SearchFilter searchFilter = SearchFilter.all();

    public SearchQuery(String collection, QueryOperation queryOperation) {
        super(collection, queryOperation);
    }

    @Override
    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }
}
