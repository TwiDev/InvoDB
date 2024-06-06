package ch.twidev.invodb.mapper;

import ch.twidev.invodb.mapper.InvoSchema;

public abstract class IndexedInvoSchema<PrimaryKey> extends InvoSchema {

    private final String primaryKey;

    public IndexedInvoSchema(String primaryKey, String collection) {
        super(collection);

        this.primaryKey = primaryKey;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public abstract PrimaryKey getPrimaryValue();
}
