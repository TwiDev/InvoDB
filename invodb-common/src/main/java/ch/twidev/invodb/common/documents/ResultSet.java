package ch.twidev.invodb.common.documents;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;

import java.util.Iterator;

public class ResultSet implements ElementSet {


    @Override
    public Iterator<Elements> iterator() {
        return null;
    }

    @Override
    public long getTime() {
        return 0;
    }
}
