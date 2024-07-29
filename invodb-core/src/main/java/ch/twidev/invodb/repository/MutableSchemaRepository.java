package ch.twidev.invodb.repository;

import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.repository.annotations.Aspect;

public interface MutableSchemaRepository<AspectSchema extends AspectInvoSchema<A>, A> extends SchemaRepository<AspectSchema> {

    @Aspect
    A getAspect(SearchFilter searchFilter);

}
