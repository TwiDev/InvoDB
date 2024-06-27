package ch.twidev.invodb.common.session;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.ElementSetWrapper;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.operations.OperationContext;
import ch.twidev.invodb.bridge.operations.SearchContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.DeleteOperationBuilder;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.builder.InsertOperationBuilder;
import ch.twidev.invodb.common.query.builder.UpdateOperationBuilder;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class DriverConnection {

    public static <Session, R> R runQuery(DriverSession<Session> session, InvoQuery<R> invoQuery, PlaceholderContext placeholderContext) {
        if(!(invoQuery instanceof OperationContext operationContext)) return null;

        if(placeholderContext == null) {
            placeholderContext = operationContext.getPlaceHolder();
        }

        if(operationContext instanceof SearchContext searchContext) {
            if (invoQuery.getQueryOperation().isCacheable()) {
                ElementSet<?> cachedValue = findCachedValues(session, operationContext, searchContext);

                if(cachedValue != null) return (R) cachedValue;
            }
        }

        R result = switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> (R) session.find(findOperationBuilder, placeholderContext);
            case UpdateOperationBuilder updateOperationBuilder -> (R) session.update(updateOperationBuilder, placeholderContext);
            case InsertOperationBuilder insertOperationBuilder -> (R) session.insert(insertOperationBuilder, placeholderContext);
            case DeleteOperationBuilder deleteOperationBuilder -> (R) session.delete(deleteOperationBuilder, placeholderContext);

            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        };

        if(operationContext instanceof SearchContext searchContext) {
            if(invoQuery.getQueryOperation().isCacheable()) {
                if(!(result instanceof ElementSet<?> elementSet)) {
                    return null;
                }

                putCachedValues(session, operationContext, searchContext, elementSet.getWrapper());

                // Cause of exhausted iterator
                return (R) findCachedValues(session, operationContext, searchContext);
            }else if(invoQuery.getQueryOperation().isInvalidateCache()) {
                invalidateCachedValues(session, operationContext, searchContext);
            }
        }

        return result;

    }

    public static <Session, R> CompletableFuture<R> runQueryAsync(DriverSession<Session> session, InvoQuery<R> invoQuery, PlaceholderContext placeholderContext) {
        if(!(invoQuery instanceof OperationContext operationContext)) return null;

        if(placeholderContext == null) {
            placeholderContext = operationContext.getPlaceHolder();
        }

        if(operationContext instanceof SearchContext searchContext) {
            if (invoQuery.getQueryOperation().isCacheable()) {
                ElementSet<?> cachedValue = findCachedValues(session, operationContext, searchContext);

                if(cachedValue != null) return CompletableFuture.completedFuture((R) cachedValue);
            }
        }

        CompletableFuture<R> completableFuture = switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> (CompletableFuture<R>) session.findAsync(findOperationBuilder, placeholderContext);
            case UpdateOperationBuilder updateOperationBuilder -> (CompletableFuture<R>) session.updateAsync(updateOperationBuilder, placeholderContext);
            case InsertOperationBuilder insertOperationBuilder -> (CompletableFuture<R>) session.insertAsync(insertOperationBuilder, placeholderContext);
            case DeleteOperationBuilder deleteOperationBuilder -> (CompletableFuture<R>) session.deleteAsync(deleteOperationBuilder, placeholderContext);

            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        };

        if(operationContext instanceof SearchContext searchContext) {
            completableFuture.whenComplete((result, throwable) -> {
                if(throwable != null) return;

                if(!(result instanceof ElementSet<?> elementSet)) {
                    return;
                }

                if(invoQuery.getQueryOperation().isCacheable()) {
                    putCachedValues(session, operationContext, searchContext, elementSet.getWrapper());
                }else if(invoQuery.getQueryOperation().isInvalidateCache()){
                    invalidateCachedValues(session, operationContext, searchContext);
                }
            });
        }

        return completableFuture;
    }

    public static <Session> ElementSet<?> findCachedValues(DriverSession<Session> session, OperationContext operationContext, SearchContext searchContext) {
        QueryCache<Session> cache = getQueryCache(session);
        if(cache == null) return null;

        final int searchCode = searchContext.getSearchFilter().getTotalHashCode();

        if(cache.has(searchCode)) {
           return cache.getSet(searchCode);
        }

        return null;
    }

    public static <Session> void putCachedValues(DriverSession<Session> session, OperationContext operationContext, SearchContext searchContext, ElementSetWrapper<? extends Elements> value) {
        QueryCache<Session> cache = getQueryCache(session);
        if(cache == null) return;
        if(value == null) return;

        final int opcode = searchContext.getSearchFilter().getTotalHashCode();

        cache.put(opcode, value);
    }

    public static <Session> void invalidateCachedValues(DriverSession<Session> session, OperationContext operationContext, SearchContext searchContext) {
        QueryCache<Session> cache = getQueryCache(session);
        if(cache == null) return;

        cache.remove(
                searchContext.getSearchFilter().getTotalHashCode()
        );
    }

    public static <Session> QueryCache<Session> getQueryCache(DriverSession<Session> session) {
        Cache<?,?> cache = session.getQueryCache();
        if(cache == null) return null;
        if(cache instanceof QueryCache<?> queryCache) {
            return (QueryCache<Session>) queryCache;
        }

        return null;
    }

}
