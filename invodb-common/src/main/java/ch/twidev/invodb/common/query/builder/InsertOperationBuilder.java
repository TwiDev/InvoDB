package ch.twidev.invodb.common.query.builder;

import ch.twidev.invodb.bridge.contexts.FieldMap;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.InsertContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.FieldsOperation;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;

import java.util.ArrayList;
import java.util.List;

public class InsertOperationBuilder extends InvoQuery<OperationResult> implements InsertContext, FieldsOperation<InsertOperationBuilder> {

    private final FieldMap fieldMap = new FieldMap();
    private PlaceholderContext placeholderContext = new PlaceholderContext();
    private boolean ifExists = true;

    public InsertOperationBuilder(String collection) {
        super(OperationResult.class, collection, QueryOperation.INSERT);
    }

    @Override
    public InsertOperationBuilder field(String key, Object value) {
        fieldMap.put(key, value);

        return this;
    }

    @Override
    public FieldMap getFields() {
        return fieldMap;
    }

    public InsertOperationBuilder insertIfAlreadyExists(boolean b) {
        this.ifExists = b;

        return this;
    }

    @Override
    public boolean ifExists() {
        return ifExists;
    }

    @Override
    public List<Object> getContexts() {
        return new ArrayList<>();
    }

    public InsertOperationBuilder setPlaceholderContext(PlaceholderContext placeholderContext) {
        this.placeholderContext = placeholderContext;

        return this;
    }

    @Override
    public PlaceholderContext getPlaceHolder() {
        return placeholderContext;
    }
}
