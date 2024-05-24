package ch.twidev.invodb.common.query.builder;

import ch.twidev.invodb.common.query.Attributes;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.AttributeOperation;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.common.query.operations.search.SearchOperation;
import ch.twidev.invodb.common.result.ResultSet;

public class FindOperationBuilder extends InvoQuery<ResultSet> implements AttributeOperation<FindOperationBuilder>, SearchOperation<FindOperationBuilder> {

    private final Attributes attributes = new Attributes();
    private SearchFilter searchFilter = SearchFilter.all();

    public FindOperationBuilder(String collection) {
        super(ResultSet.class, collection, QueryOperation.FIND);
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
