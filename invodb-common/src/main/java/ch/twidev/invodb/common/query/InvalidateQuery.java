package ch.twidev.invodb.common.query;

import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.common.query.operations.QueryOperation;

public abstract class InvalidateQuery<Result, Query> extends SearchQuery<Result, Query> {
    public InvalidateQuery(String collection, QueryOperation queryOperation) {
        super(collection, queryOperation);
    }

    @Override
    public Result handleResult(DriverSession<?> driverSession, Result result) {
        QueryCache<?> queryCache = getQueryCache(driverSession);

        this.invalidateCachedValues(queryCache);

        return super.handleResult(driverSession, result);
    }

    public void invalidateCachedValues(QueryCache<?> cache) {
        if(cache == null) return;

        cache.remove(
                this.getSearchFilter().getTotalHashCode()
        );
    }
}
