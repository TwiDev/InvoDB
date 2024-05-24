package ch.twidev.invodb.common.query.operations;

public interface SearchOperation<Builder> {

    public Builder where(SearchFilter searchFilter);

    SearchFilter getSearchFilter();

}
