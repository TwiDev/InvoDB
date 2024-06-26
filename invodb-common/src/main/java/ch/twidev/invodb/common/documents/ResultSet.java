package ch.twidev.invodb.common.documents;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;

import java.util.Iterator;

public class ResultSet extends ElementSet<Elements> {

    public ResultSet(Iterator<Elements> iterator) {
        super(iterator, iterator.next());
    }

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

    @Override
    public Elements first() {
        return null;
    }

    @Override
    public ElementSet<Elements> fromElements() {
        return null;
    }

}
