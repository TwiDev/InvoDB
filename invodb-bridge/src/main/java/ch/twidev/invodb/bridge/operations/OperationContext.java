package ch.twidev.invodb.bridge.operations;

import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;

import java.util.HashMap;
import java.util.List;

public interface OperationContext {

    String getCollection();

    List<Object> getContexts();

    PlaceholderContext getPlaceHolder();

    int operationHashCode();
}
