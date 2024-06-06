package ch.twidev.invodb.mapper;

import ch.twidev.invodb.mapper.handler.SchemaAspectHandler;

import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public abstract class AspectInvoSchema<Aspect, PrimaryKey> extends IndexedInvoSchema<PrimaryKey> {

    private final Aspect aspect;

    public AspectInvoSchema(Class<Aspect> aspectInterface, String primaryKey, String collection) {
        super(primaryKey, collection);

        if(!aspectInterface.isInterface()) {
            throw new IllegalArgumentException("Aspect provided isn't an interface");
        }

        if(!this.getClass().getSuperclass().getSuperclass().isAssignableFrom(aspectInterface)) {
            throw new IllegalArgumentException("This schema isn't implemented by this aspect");
        }

        SchemaAspectHandler schemaAspectHandler = new SchemaAspectHandler(this);

        this.aspect = (Aspect) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{aspectInterface},
                schemaAspectHandler);
    }

    public Aspect getAspect() {
        return aspect;
    }
}
