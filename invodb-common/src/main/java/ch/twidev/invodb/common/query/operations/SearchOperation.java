package ch.twidev.invodb.common.query.operations;

import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;

public interface SearchOperation<Builder> {

    public Builder where(SearchFilter searchFilter);

    public Builder setPlaceholder(PlaceholderContext placeholderContext);

    SearchFilter getSearchFilter();

    PlaceholderContext getPlaceHolder();

}
