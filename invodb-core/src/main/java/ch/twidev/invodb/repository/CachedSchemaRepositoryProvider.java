package ch.twidev.invodb.repository;

import ch.twidev.invodb.bridge.cache.MapCache;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.mapper.InvoSchema;

public abstract class CachedSchemaRepositoryProvider<Session, Schema extends InvoSchema, Provider extends SchemaRepository<Schema>> extends SchemaRepositoryProvider<Session,Schema,Provider> {

    private final MapCache<?> cache;

    public CachedSchemaRepositoryProvider(DriverSession<Session> driverSession, String collection, Class<Provider> classInterface, MapCache<?> cache) {
        super(driverSession, collection, classInterface);

        this.cache = cache;
    }

    public MapCache<?> getCache() {
        return cache;
    }
}
