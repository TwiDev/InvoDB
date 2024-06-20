package ch.twidev.invodb.common.query.builder;

import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.DeleteContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.query.operations.SearchOperation;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import com.google.common.hash.HashCode;

import java.util.List;

public class DeleteOperationBuilder extends InvoQuery<OperationResult> implements DeleteContext, SearchOperation<DeleteOperationBuilder> {

    private SearchFilter searchFilter = SearchFilter.all();
    private PlaceholderContext placeholderContext = new PlaceholderContext();

    public DeleteOperationBuilder(String collection) {
        super(OperationResult.class, collection, QueryOperation.DELETE);
    }

    @Override
    public List<Object> getContexts() {
        return searchFilter.getContexts();
    }

    @Override
    public DeleteOperationBuilder where(SearchFilter searchFilter) {
        this.searchFilter =searchFilter;

        return this;
    }

    @Override
    public DeleteOperationBuilder setPlaceholder(PlaceholderContext placeholderContext) {
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
    public int operationHashCode() {
        return hashCode();
    }
}
