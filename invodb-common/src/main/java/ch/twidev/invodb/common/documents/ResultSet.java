package ch.twidev.invodb.common.documents;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;

import java.util.Iterator;

public class ResultSet implements ElementSet {

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Elements next() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
