package ch.twidev.invodb.bridge.search;

import ch.twidev.invodb.bridge.contexts.FieldMap;

public interface SearchCondition {

    boolean evaluate(FieldMap searchField);

}
