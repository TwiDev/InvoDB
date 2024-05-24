package ch.twidev.invodb.common.query.operations;

import ch.twidev.invodb.common.query.FieldMap;

public interface FieldsOperation<Builder> {

    Builder field(String key, String value);

    FieldMap getFields();

}
