package ch.twidev.invodb.mapper;

public abstract class IndexedInvoSchema<PrimaryKey> extends InvoSchema {

    private final String primaryKey;

    public IndexedInvoSchema(String primaryKey) {

        this.primaryKey = primaryKey;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public abstract PrimaryKey getPrimaryValue();
}
