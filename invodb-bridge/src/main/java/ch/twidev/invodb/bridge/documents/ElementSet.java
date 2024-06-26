package ch.twidev.invodb.bridge.documents;

import java.io.Serializable;
import java.util.Iterator;

public abstract class ElementSet<Raw> implements Iterator<Elements>, OperationResult, Serializable {

    private transient final Iterator<Elements> iterator;
    private final Raw elements;

    public ElementSet(Iterator<Elements> iterator, Raw elements) {
        this.iterator = iterator;
        this.elements = elements;
    }

    public abstract boolean isEmpty();

    public abstract Elements first();

    public abstract ElementSet<Raw> fromElements();

    public Raw getElements() {
        return elements;
    }

    @Override
    public Elements next() {
        return iterator.next();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}