package ch.twidev.invodb.common.result;

import ch.twidev.invodb.bridge.result.Element;
import ch.twidev.invodb.bridge.result.ElementSet;

import java.util.Iterator;

public class ResultSet implements OperationResult, ElementSet {
    @Override
    public Iterator<Element> iterator() {
        return null;
    }
}
