package ch.twidev.invodb.repository;

import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.InvoSchema;
import ch.twidev.invodb.mapper.annotations.PrimaryField;
import ch.twidev.invodb.repository.handler.SchemaRepositoryHandler;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public abstract class SchemaRepositoryProvider<Session, Schema extends InvoSchema, Provider extends SchemaRepository<Schema>> {

    private final DriverSession<Session> driverSession;
    private final Class<Provider> classInterface;
    private final Class<Schema> schemaClass;

    private final String collection;
    private String primaryField = null;
    private AspectInvoSchema<?,?> blankSchema = null;

    public SchemaRepositoryProvider(DriverSession<Session> driverSession, String collection, Class<Provider> classInterface) {
        this.driverSession = driverSession;
        this.collection = collection;

        if(!classInterface.isInterface()) {
            throw new IllegalArgumentException(classInterface.getName() + " isn't an interface");
        }

        this.classInterface = classInterface;
        this.schemaClass = (Class<Schema>) new TypeToken<Schema>(this.getClass()){}.getRawType();

        for (Field declaredField : this.schemaClass.getDeclaredFields()) {
            if(declaredField.isAnnotationPresent(PrimaryField.class)) {
                ch.twidev.invodb.mapper.annotations.Field field = declaredField.getAnnotation(ch.twidev.invodb.mapper.annotations.Field.class);
                primaryField = field.name().isEmpty() ? declaredField.getName() : field.name();
                break;
            }
        }

        if(schemaClass.getSuperclass().isAssignableFrom(AspectInvoSchema.class)) {
            if(primaryField == null) {
                throw new NullPointerException("Cannot find any primary field for aspect schema " + schemaClass);
            }

            try {
                this.blankSchema = (AspectInvoSchema<?, ?>) schemaClass.getConstructor().newInstance();

                this.blankSchema.setCollection(collection);
                this.blankSchema.setDriverSession(driverSession);
                this.blankSchema.setExists(true);

                this.blankSchema.load();

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Provider build() {
        return (Provider) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{classInterface},
                new SchemaRepositoryHandler<>(this));
    }

    public Class<Provider> getClassInterface() {
        return classInterface;
    }

    public DriverSession<Session> getDriverSession() {
        return driverSession;
    }

    public Class<Schema> getSchema() {
        return schemaClass;
    }

    public String getCollection() {
        return collection;
    }

    public String getPrimaryField() {
        return primaryField;
    }

    public AspectInvoSchema<?, ?> getBlankSchema() {
        return blankSchema;
    }
}
