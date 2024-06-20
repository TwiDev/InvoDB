package ch.twidev.invodb.common.session;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.OperationContext;
import ch.twidev.invodb.bridge.operations.SearchContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ResultCallback;

import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.DeleteOperationBuilder;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.builder.InsertOperationBuilder;
import ch.twidev.invodb.common.query.builder.UpdateOperationBuilder;

import java.io.Serializable;
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
                R cachedValue = findCachedValues(session, operationContext, searchContext);

                if(cachedValue != null) return cachedValue;
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
                putCachedValues(session, operationContext, searchContext, result);
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
                R cachedValue = findCachedValues(session, operationContext, searchContext);

                if(cachedValue != null) return CompletableFuture.completedFuture(cachedValue);
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

                if(invoQuery.getQueryOperation().isCacheable()) {
                    putCachedValues(session, operationContext, searchContext, result);
                }else if(invoQuery.getQueryOperation().isInvalidateCache()){
                    invalidateCachedValues(session, operationContext, searchContext);
                }
            });
        }

        return completableFuture;
    }

    public static <Session, R> R findCachedValues(DriverSession<Session> session, OperationContext operationContext, SearchContext searchContext) {
        QueryCache<?> cache = getQueryCache(session);
        if(cache == null) return null;

        final int opcode = operationContext.operationHashCode();
        final int searchCode = searchContext.hashCode();

        if(!cache.has(opcode)) {
            return (R) cache.get(searchCode);
        }

        return null;
    }

    public static <Session, R> void putCachedValues(DriverSession<Session> session, OperationContext operationContext, SearchContext searchContext, R value) {
        QueryCache<?> cache = getQueryCache(session);
        if(cache == null) return;
        if(value == null) return;

        if(!(value instanceof Serializable)) return;

        final int opcode = operationContext.operationHashCode();

        cache.put(opcode, value);
    }

    public static <Session> void invalidateCachedValues(DriverSession<Session> session, OperationContext operationContext, SearchContext searchContext) {
        QueryCache<?> cache = getQueryCache(session);
        if(cache == null) return;

        cache.remove(
                operationContext.operationHashCode()
        );
    }

    public static <Session> QueryCache<?> getQueryCache(DriverSession<Session> session) {
        Cache<?,?> cache = session.getQueryCache();
        if(cache == null) return null;
        if(cache instanceof QueryCache<?> queryCache) {
            return queryCache;
        }

        return null;
    }

}
