package ch.twidev.invodb.common.query.operations.search;

public interface SearchOperation<Builder> {

    public Builder where(SearchFilter searchFilter);

    SearchFilter getSearchFilter();

}
