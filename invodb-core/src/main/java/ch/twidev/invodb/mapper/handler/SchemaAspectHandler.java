package ch.twidev.invodb.mapper.handler;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.mapper.annotations.Update;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public record SchemaAspectHandler(AspectInvoSchema<?,?> invoSchema) implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InvoQuery<?> invoQuery = this.parseQuery(method, args);

        if(invoQuery != null) {
            if(method.isAnnotationPresent(Async.class)) {
                // Run Query
            }
        }

        return method.invoke(invoSchema, args);
    }

    public InvoQuery<?> parseQuery(Method method, Object[] args) {
        if(args.length == 0) return null;

        String collection = invoSchema.getCollection();

        SearchFilter searchFilter = SearchFilter.eq(invoSchema.getPrimaryKey(), invoSchema.getPrimaryValue());

        if (method.isAnnotationPresent(Update.class)) {
            Update update = method.getAnnotation(Update.class);

            return InvoQuery.update(collection)
                    .field(update.field(), args[0])
                    .where(searchFilter);
        }

        return null;
    }
}
