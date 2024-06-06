package ch.twidev.invodb.mapper.handler;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.mapper.InvoSchema;
import ch.twidev.invodb.mapper.annotations.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public record SchemaAspectHandler(InvoSchema invoSchema) implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Setter.class)) {
            InvoQuery.find(invoSchema.getCollection());
        }

        return method.invoke(invoSchema, args);
    }
}
