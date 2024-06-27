package ch.twidev.invodb.bridge.documents;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public abstract class ElementSet<Raw extends Elements> implements Iterator<Raw>, OperationResult, Serializable {

    @Serial
    private static final long serialVersionUID = 6529685098267757690L;

    protected transient Iterator<Raw> iterator;
    protected ElementSetWrapper<Raw> wrapper;

    public ElementSet(Iterator<Raw> iterator, Class<? extends ElementSetWrapper<Raw>> wrapper) {
        this.iterator = iterator;

        try {
            this.wrapper = wrapper.getDeclaredConstructor(Iterator.class).newInstance(iterator);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public ElementSet(Iterator<Raw> iterator, ElementSetWrapper<Raw> wrapper) {
        this.iterator = iterator;
        this.wrapper = wrapper;
    }

    public ElementSet() {
    }

    public abstract boolean isEmpty();

    public abstract Elements first();

    public abstract ElementSet<Raw> fromElements();

    public ElementSetWrapper<Raw> getWrapper() {
        return wrapper;
    }

    @Override
    public Raw next() {
        return iterator.next();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}