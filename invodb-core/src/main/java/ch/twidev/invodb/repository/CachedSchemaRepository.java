package ch.twidev.invodb.repository;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.mapper.InvoSchema;

public abstract class CachedSchemaRepository<Session, Schema extends InvoSchema, Provider extends SchemaRepositoryProvider<Schema>> extends SchemaRepository<Session,Schema,Provider> {

    private final Cache<?,?> cache;

    public CachedSchemaRepository(DriverSession<Session> driverSession, String collection, Class<Provider> classInterface, Cache<?,?> cache) {
        super(driverSession, collection, classInterface);

        this.cache = cache;
    }

    public Cache<?, ?> getCache() {
        return cache;
    }
}
