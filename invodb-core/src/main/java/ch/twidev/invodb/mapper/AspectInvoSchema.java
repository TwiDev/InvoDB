package ch.twidev.invodb.mapper;

import ch.twidev.invodb.mapper.handler.SchemaAspectHandler;

import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public abstract class AspectInvoSchema<Aspect> extends IndexedInvoSchema {

    private final Aspect aspect;

    public AspectInvoSchema(Class<Aspect> aspectInterface) {
        if(!aspectInterface.isInterface()) {
            throw new IllegalArgumentException("Aspect provided isn't an interface");
        }

       /* if(!this.getClass().getSuperclass().getSuperclass().isAssignableFrom(aspectInterface)) {
            throw new IllegalArgumentException("This schema isn't implemented by this aspect");
        }*/

        SchemaAspectHandler schemaAspectHandler = new SchemaAspectHandler(this, this.getPrimaryValues());

        this.aspect = (Aspect) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{aspectInterface},
                schemaAspectHandler);

    }

    public AspectInvoSchema() {
        this.aspect = null;
    }

    public Aspect getAspect() {
        return aspect;
    }
}
