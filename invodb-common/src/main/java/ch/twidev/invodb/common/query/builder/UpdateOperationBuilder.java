package ch.twidev.invodb.common.query.builder;

import ch.twidev.invodb.bridge.contexts.Attributes;
import ch.twidev.invodb.bridge.contexts.FieldMap;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.operations.UpdateContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.AttributeOperation;
import ch.twidev.invodb.common.query.operations.FieldsOperation;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.query.operations.SearchOperation;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;

import java.util.List;

public class UpdateOperationBuilder extends InvoQuery<OperationResult> implements UpdateContext, FieldsOperation<UpdateOperationBuilder>, SearchOperation<UpdateOperationBuilder> {

    private final FieldMap fieldMap = new FieldMap();
    private SearchFilter searchFilter = SearchFilter.all();
    private PlaceholderContext placeholderContext = new PlaceholderContext();

    public UpdateOperationBuilder(String collection) {
        super(OperationResult.class, collection, QueryOperation.UPDATE);
    }

    @Override
    public UpdateOperationBuilder where(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;

        return this;
    }

    @Override
    public UpdateOperationBuilder setPlaceholder(PlaceholderContext placeholderContext) {
        this.placeholderContext = placeholderContext;

        return this;
    }

    @Override
    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    @Override
    public PlaceholderContext getPlaceHolder() {
        return placeholderContext;
    }


    @Override
    public List<Object> getContexts() {
        return searchFilter.getContexts();
    }

    @Override
    public UpdateOperationBuilder field(String key, Object value) {
        fieldMap.put(key, value);

        return this;
    }

    @Override
    public FieldMap getFields() {
        return fieldMap;
    }
}
