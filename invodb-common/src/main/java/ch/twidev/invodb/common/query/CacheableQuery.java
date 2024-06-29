package ch.twidev.invodb.common.query;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.ElementSetWrapper;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.common.query.operations.QueryOperation;

import java.util.concurrent.CompletableFuture;

public abstract class CacheableQuery<Query> extends SearchQuery<ElementSet<?>, Query> {
    public CacheableQuery(String collection, QueryOperation queryOperation) {
        super(collection, queryOperation);
    }

    @Override
    protected ElementSet<?> execute(DriverSession<?> driverSession, PlaceholderContext placeholderContext) {
        QueryCache<?> queryCache = getQueryCache(driverSession);
        ElementSet<?> cachedValue = this.findCachedValues(queryCache);

        if(cachedValue != null) return cachedValue;

        return this.handleExecute(driverSession, placeholderContext);
    }

    @Override
    protected CompletableFuture<ElementSet<?>> executeAsync(DriverSession<?> driverSession, PlaceholderContext placeholderContext) {
        QueryCache<?> queryCache = getQueryCache(driverSession);
        ElementSet<?> cachedValue = this.findCachedValues(queryCache);

        if(cachedValue != null) return CompletableFuture.completedFuture(cachedValue);

        return this.handleExecuteAsync(driverSession, placeholderContext);
    }

    public abstract ElementSet<?> handleExecute(DriverSession<?> driverSession, PlaceholderContext placeholderContext);

    public abstract CompletableFuture<ElementSet<?>> handleExecuteAsync(DriverSession<?> driverSession, PlaceholderContext placeholderContext);

    @Override
    public ElementSet<?> handleResult(DriverSession<?> driverSession, ElementSet<?> result) {
        QueryCache<?> queryCache = getQueryCache(driverSession);

        if(queryCache == null)
            return super.handleResult(driverSession, result);

        if(!result.isCached()) {
            /* Iterator get exhausted because of the init of the wrapper */
            this.putCachedValues(queryCache, result.getWrapper());

            System.out.println("ttt");

            return super.handleResult(driverSession,
                    this.findCachedValues(queryCache));
        }

        return super.handleResult(driverSession, result);
    }

    public ElementSet<?> findCachedValues(QueryCache<?> cache) {
        if(cache == null) return null;

        final int searchCode = this.getSearchFilter().getTotalHashCode();

        if(cache.has(searchCode)) {
            return cache.getSet(searchCode);
        }

        return null;
    }

    public void putCachedValues(QueryCache<?> cache, ElementSetWrapper<? extends Elements> value) {
        if(cache == null) return;
        if(value == null) return;

        final int opcode = this.getSearchFilter().getTotalHashCode();

        cache.put(opcode, value);
    }
}
