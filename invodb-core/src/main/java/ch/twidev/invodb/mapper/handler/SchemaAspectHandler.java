package ch.twidev.invodb.mapper.handler;

import ch.twidev.invodb.bridge.util.ResultCallback;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.mapper.annotations.Update;
import ch.twidev.invodb.mapper.field.FieldMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public record SchemaAspectHandler(AspectInvoSchema<?,?> invoSchema) implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InvoQuery<?> invoQuery = this.parseQuery(method, args);

        if(invoQuery != null) {
            if(method.isAnnotationPresent(Async.class)) {
                SchemaOperationHandler<?> resultCallback = (invoSchema instanceof SchemaOperationHandler<?> handler) ? handler : o -> {};

                invoQuery.runAsync(invoSchema.getDriverSession()).exceptionally(throwable -> {
                    resultCallback.onFailed(throwable);

                    return null;
                });
            }else{
                invoQuery.run(invoSchema.getDriverSession());
            }
        }

        return method.invoke(invoSchema, args);
    }

    public InvoQuery<?> parseQuery(Method method, Object[] args) {
        if(args.length == 0 || !invoSchema.isExists()) return null;

        String collection = invoSchema.getCollection();

        SearchFilter searchFilter = SearchFilter.eq(
                invoSchema.getPrimaryKey(),
                invoSchema.getPrimaryValue()
        );

        if (method.isAnnotationPresent(Update.class)) {
            Update update = method.getAnnotation(Update.class);
            String fieldName = update.field();

            if(!invoSchema.getFields().containsKey(fieldName)) return null;
            FieldMapper fieldMapper = invoSchema.getFields().get(fieldName);

            return InvoQuery.update(collection)
                    .field(fieldMapper.queryName(), fieldMapper.getFormattedValue(args[0]))
                    .where(searchFilter);
        }

        return null;
    }
}
