package ch.twidev.invodb.bridge.operations;

import ch.twidev.invodb.bridge.contexts.Attributes;
import ch.twidev.invodb.bridge.search.ISearchFilter;

public interface FindContext extends OperationContext, SearchContext {

    Attributes getAttributes();

}
