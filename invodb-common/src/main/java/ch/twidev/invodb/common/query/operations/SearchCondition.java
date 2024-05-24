package ch.twidev.invodb.common.query.operations;

import ch.twidev.invodb.common.query.FieldMap;

public interface SearchCondition {

    boolean evaluate(FieldMap searchField);

}
