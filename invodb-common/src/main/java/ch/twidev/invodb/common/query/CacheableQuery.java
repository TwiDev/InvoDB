package ch.twidev.invodb.common.query;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.ElementSetWrapper;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.operations.SearchContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import org.jetbrains.annotations.NotNull;

public abstract class CacheableQuery<Result> extends InvoQuery<Result> {
    public CacheableQuery(String collection, QueryOperation queryOperation) {
        super(collection, queryOperation);
    }

    @Override
    public Result run(@NotNull DriverSession<?> driverConnection, PlaceholderContext placeholderContext) {
        return super.run(driverConnection, placeholderContext);
    }

    public static <Session> ElementSet<?> findCachedValues(DriverSession<Session> session, SearchContext searchContext) {
        QueryCache<Session> cache = getQueryCache(session);
        if(cache == null) return null;

        final int searchCode = searchContext.getSearchFilter().getTotalHashCode();

        if(cache.has(searchCode)) {
            return cache.getSet(searchCode);
        }

        return null;
    }

    public static <Session> void putCachedValues(DriverSession<Session> session, SearchContext searchContext, ElementSetWrapper<? extends Elements> value) {
        QueryCache<Session> cache = getQueryCache(session);
        if(cache == null) return;
        if(value == null) return;

        final int opcode = searchContext.getSearchFilter().getTotalHashCode();

        cache.put(opcode, value);
    }

    public static <Session> void invalidateCachedValues(DriverSession<Session> session, SearchContext searchContext) {
        QueryCache<Session> cache = getQueryCache(session);
        if(cache == null) return;

        cache.remove(
                searchContext.getSearchFilter().getTotalHashCode()
        );
    }
}
