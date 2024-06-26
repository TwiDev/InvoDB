package ch.twidev.invodb.bridge.documents;

import java.util.Iterator;

public class SingleElementSet extends ElementSet<Object> {

    public Elements elements;

    public SingleElementSet(Iterator<Elements> iterator, Object elements) {
        super(iterator, elements);
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
        return elements;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Elements first() {
        return elements;
    }

    @Override
    public ElementSet<Object> fromElements() {
        return null;
    }
}
