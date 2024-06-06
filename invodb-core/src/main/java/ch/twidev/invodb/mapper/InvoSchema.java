package ch.twidev.invodb.mapper;

public abstract class InvoSchema {

    private final String collection;

    public InvoSchema(String collection) {
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

}
