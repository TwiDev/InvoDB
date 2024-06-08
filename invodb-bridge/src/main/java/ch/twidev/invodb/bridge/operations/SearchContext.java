package ch.twidev.invodb.bridge.operations;

import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.search.ISearchFilter;

public interface SearchContext {

    ISearchFilter getSearchFilter();

}
