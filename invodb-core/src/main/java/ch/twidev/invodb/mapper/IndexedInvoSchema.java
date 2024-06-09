package ch.twidev.invodb.mapper;

import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.common.query.builder.UpdateOperationBuilder;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;

import java.util.concurrent.CompletableFuture;

public abstract class IndexedInvoSchema<PrimaryKey> extends InvoSchema {

    private final String primaryKey;

    public IndexedInvoSchema(String primaryKey) {

        this.primaryKey = primaryKey;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public abstract PrimaryKey getPrimaryValue();

    public void save() {
        this.save(this.getPrimaryFilter());
    }

    public CompletableFuture<OperationResult> saveAsync() {
        return this.saveAsync(this.getPrimaryFilter());
    }

    public void delete() {
        this.delete(this.getPrimaryFilter());
    }

    public void deleteAsync() {
        this.delete(this.getPrimaryFilter());
    }

    private SearchFilter getPrimaryFilter(){
        return SearchFilter.eq(primaryKey, this.getPrimaryValue());
    }
}
