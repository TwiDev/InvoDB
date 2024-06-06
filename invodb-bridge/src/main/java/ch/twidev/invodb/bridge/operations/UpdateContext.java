package ch.twidev.invodb.bridge.operations;

import ch.twidev.invodb.bridge.contexts.FieldMap;
import ch.twidev.invodb.bridge.search.ISearchFilter;

public interface UpdateContext extends OperationContext{

    ISearchFilter getSearchFilter();

    FieldMap getFields();

}
