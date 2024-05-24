package ch.twidev.invodb.common.query.operations;

import ch.twidev.invodb.common.query.Attributes;

public interface AttributeOperation<Builder> {

    Attributes getAttributes();

    Builder attribute(String attribute);

}
