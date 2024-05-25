package ch.twidev.invodb.common.query.operations;

import ch.twidev.invodb.bridge.contexts.FieldMap;

public interface FieldsOperation<Builder> {

    Builder field(String key, String value);

    FieldMap getFields();

}
