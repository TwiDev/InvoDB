package ch.twidev.invodb.common.query.builder;

import ch.twidev.invodb.bridge.contexts.Attributes;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.query.CacheableQuery;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.AttributeOperation;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.common.query.operations.SearchOperation;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FindOperationBuilder extends CacheableQuery<FindOperationBuilder>
        implements FindContext, AttributeOperation<FindOperationBuilder> {

    private final Attributes attributes = new Attributes();
    private int limit = -1;

    public FindOperationBuilder(String collection) {
        super(collection, QueryOperation.FIND);
    }

    @Override
    public FindOperationBuilder where(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;

        return this;
    }

    @Override
    public FindOperationBuilder setPlaceholder(PlaceholderContext placeholderContext) {
        this.placeholderContext = placeholderContext;

        return this;
    }

    @Override
    public PlaceholderContext getPlaceHolder() {
        return placeholderContext;
    }

    @Override
    public int operationHashCode() {
        return this.hashCode();
    }

    @Override
    public Attributes getAttributes() {
        if(attributes.isEmpty()){
            return Attributes.ALL;
        }

        return attributes;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public FindOperationBuilder attribute(String attribute) {
        attributes.add(attribute);

        return this;
    }

    @Override
    public List<Object> getContexts() {
        return searchFilter.getContexts();
    }

    public FindOperationBuilder limit(int limit) {
        this.limit = limit;

        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;

        return prime * (
                this.getCollection().hashCode() +
                this.getAttributes().hashCode() +
                this.getSearchFilter().getTotalHashCode() +
                this.getContexts().hashCode() + this.getPlaceHolder().hashCode());
    }

    @Override
    public ElementSet<?> handleExecute(DriverSession<?> driverSession, PlaceholderContext placeholderContext) {
        return driverSession.find(this, placeholderContext);
    }

    @Override
    public CompletableFuture<ElementSet<?>> handleExecuteAsync(DriverSession<?> driverSession, PlaceholderContext placeholderContext) {
        return driverSession.findAsync(this, placeholderContext);
    }
}
