package ch.twidev.invodb.common.query.operations;

import ch.twidev.invodb.common.query.operations.search.SearchFilter;

public interface SearchOperation<Builder> {

    public Builder where(SearchFilter searchFilter);

    SearchFilter getSearchFilter();

}
