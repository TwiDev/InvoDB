package ch.twidev.invodb.common.query.builder;

import ch.twidev.invodb.bridge.contexts.Attributes;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.AttributeOperation;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.common.query.operations.SearchOperation;
import ch.twidev.invodb.common.documents.ResultSet;

public class FindOperationBuilder extends InvoQuery<ElementSet> implements FindContext, AttributeOperation<FindOperationBuilder>, SearchOperation<FindOperationBuilder> {

    private final Attributes attributes = new Attributes();
    private SearchFilter searchFilter = SearchFilter.all();

    public FindOperationBuilder(String collection) {
        super(ElementSet.class, collection, QueryOperation.FIND);
    }

    @Override
    public FindOperationBuilder where(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;

        return this;
    }

    @Override
    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    @Override
    public Attributes getAttributes() {
        if(attributes.isEmpty()){
            return Attributes.ALL;
        }

        return attributes;
    }

    @Override
    public FindOperationBuilder attribute(String attribute) {
        attributes.add(attribute);

        return this;
    }
}
