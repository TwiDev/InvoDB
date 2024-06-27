package ch.twidev.invodb.bridge.documents;

import java.io.Serial;
import java.io.Serializable;
import java.util.Iterator;

public abstract class ElementSetWrapper<Raw> implements Serializable {

    @Serial
    private static final long serialVersionUID = -687991492884005033L;

    protected transient Iterator<Raw> elements;

    public ElementSetWrapper(Iterator<Raw> elements) {
        this.elements = elements;
    }

    public void setElement(Iterator<Raw> elements) {
        this.elements = elements;
    }

    public Iterator<Raw> getElements() {
        return elements;
    }
}
