package ch.twidev.invodb.repository;

import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.mapper.InvoSchema;
import ch.twidev.invodb.mapper.annotations.PrimaryField;
import ch.twidev.invodb.repository.handler.SchemaRepositoryHandler;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public abstract class SchemaRepositoryProvider<Session, Schema extends InvoSchema, Provider extends SchemaRepository<Schema>> {

    private final DriverSession<Session> driverSession;
    private final Class<Provider> classInterface;
    private final Class<Schema> schemaClass;

    private final String collection;
    private String primaryField = null;

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
}
