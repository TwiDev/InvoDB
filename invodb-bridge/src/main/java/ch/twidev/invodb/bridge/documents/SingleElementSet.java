package ch.twidev.invodb.bridge.documents;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

public class SingleElementSet implements ElementSet {

    public Elements elements;

    public SingleElementSet(Elements elements) {
        this.elements = elements;
    }

    @Override
    @NotNull

    public Iterator<Elements> iterator() {
        return Collections.singleton(elements).iterator();
    }

    @Override
    public long getTime() {
        return 0;
    }
}
