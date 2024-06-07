package ch.twidev.invodb.repository;

import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.mapper.InvoSchema;
import ch.twidev.invodb.repository.annotations.Find;
import ch.twidev.invodb.repository.handler.SchemaRepositoryHandler;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public abstract class SchemaRepository<Session, Schema extends InvoSchema, Provider extends SchemaRepositoryProvider<Schema>> {

    private final DriverSession<Session> driverSession;
    private final Class<Provider> classInterface;
    private final Class<Schema> schemaClass;

    private final String collection;

    public SchemaRepository(DriverSession<Session> driverSession, String collection, Class<Provider> classInterface) {
        this.driverSession = driverSession;
        this.collection = collection;

        if(!classInterface.isInterface()) {
            throw new IllegalArgumentException(classInterface.getName() + " isn't an interface");
        }

        this.classInterface = classInterface;
        this.schemaClass = (Class<Schema>) new TypeToken<Schema>(this.getClass()){}.getRawType();
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
}
