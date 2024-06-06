package ch.twidev.invodb.bridge.documents;

public class SingleElementSet implements ElementSet {

    public Elements elements;

    public SingleElementSet(Elements elements) {
        this.elements = elements;
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
}
