package ch.twidev.invodb.common.documents;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.ElementSetWrapper;
import ch.twidev.invodb.bridge.documents.Elements;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class CachedResultSet extends ElementSet<Elements> {

    public CachedResultSet(Iterator<? extends Elements> iterator, ElementSetWrapper<? extends Elements> wrapper) {
        super((Iterator<Elements>) iterator, (ElementSetWrapper<Elements>) wrapper);
    }

    @Override
    public boolean isCached() {
        return wrapper != null;
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return this.hasNext();
    }

    @Override
    public Elements first() {
        return iterator.next();
    }

    @Override
    public ElementSet<Elements> fromElements() {
        return new CachedResultSet(wrapper.getElements(), wrapper);
    }

}
