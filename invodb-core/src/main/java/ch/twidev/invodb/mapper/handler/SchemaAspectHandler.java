package ch.twidev.invodb.mapper.handler;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.exception.InvalidRepositoryQueryException;
import ch.twidev.invodb.exception.InvalidSchemaException;
import ch.twidev.invodb.exception.SchemaException;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.repository.annotations.Update;
import ch.twidev.invodb.mapper.field.FieldMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public record SchemaAspectHandler(AspectInvoSchema<?,?> invoSchema, Object... primaryValues) implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InvoQuery<?> invoQuery = this.parseQuery(method, args);

        if(invoQuery != null) {
            if(method.isAnnotationPresent(Async.class)) {
                SchemaOperationHandler resultCallback = (invoSchema instanceof SchemaOperationHandler handler) ? handler : SchemaOperationHandler.HANDLER;

                invoQuery.runAsync(invoSchema.getDriverSession()).whenComplete((o, throwable) -> {
                    if(throwable != null) {
                        resultCallback.onFailed(throwable);
                        return;
                    }

                    resultCallback.onSuccess(invoQuery);
                });
            }else{
                invoQuery.run(invoSchema.getDriverSession());
            }
        }

        return method.invoke(invoSchema, args);
    }

    public InvoQuery<?> parseQuery(Method method, Object[] args) throws SchemaException {
        String collection = invoSchema.getCollection();

        SearchFilter searchFilter;

        if(args[0] instanceof SearchFilter filter) {
            searchFilter = filter;
        }else{
            searchFilter = invoSchema.getPrimaryFilters(primaryValues);
        }

        if (method.isAnnotationPresent(Update.class)) {
            this.checkQuery(args);

            Update update = method.getAnnotation(Update.class);
            String fieldName = update.field();

            if(!invoSchema.getFields().containsKey(fieldName))
                throw new InvalidRepositoryQueryException("Invalid field " + fieldName + ", not declared in schema " + invoSchema.getCollection());

            FieldMapper fieldMapper = invoSchema.getFields().get(fieldName);

            return InvoQuery.update(collection)
                    .field(fieldMapper.queryName(), fieldMapper.getFormattedValue(args[0]))
                    .where(searchFilter);
        }

        return null;
    }

    public void checkQuery(Object[] args) throws SchemaException {
        if(args == null || args.length == 0) {
            throw new InvalidRepositoryQueryException("You need to define annotation arguments in the method to define repository functions");
        }

        if(!invoSchema.exists()) {
            throw new InvalidSchemaException("Cannot change aspect of " + invoSchema.getCollection() + " schema because it isn't populate");
        }
    }
}
