package ch.twidev.invodb.driver.mongodb;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.documents.SingleElementSet;
import ch.twidev.invodb.bridge.operations.DeleteContext;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.operations.InsertContext;
import ch.twidev.invodb.bridge.operations.UpdateContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.driver.mongodb.filter.BsonFilter;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MongoConnection implements DriverSession<MongoDatabase> {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private final MongoDatabase mongoDatabase;
    private final Cache<?,?> queryCache;

    public MongoConnection(MongoDatabase mongoDatabase, Cache<?,?> queryCache) {
        this.mongoDatabase = mongoDatabase;
        this.queryCache = queryCache;
    }

    @Override
    public Cache<?, ?> getQueryCache() {
        return queryCache;
    }

    @Override
    public ElementSet<?> find(FindContext findOperationBuilder, PlaceholderContext placeholderContext) {
        Bson searchFilter = BsonFilter.toBson(findOperationBuilder, placeholderContext);

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(findOperationBuilder.getCollection());
        FindIterable<Document> document = ((searchFilter != null) ? mongoCollection.find(searchFilter) : mongoCollection.find());

        return new MongoResultSet(document.iterator());
    }

    @Override
    public CompletableFuture<ElementSet<?>> findAsync(FindContext findOperationBuilder, PlaceholderContext placeholderContext) {
        return CompletableFuture.supplyAsync(() -> this.find(findOperationBuilder, placeholderContext), executor);
    }

    @Override
    public OperationResult update(UpdateContext updateContext, PlaceholderContext placeholderContext) {
        Bson searchFilter = BsonFilter.toBson(updateContext, placeholderContext);

        try {
            Bson updates = Updates.combine(
                    updateContext.getFields().getFormattedFields(placeholderContext).entrySet()
                            .stream()
                            .map(entry -> Updates.set(entry.getKey(), entry.getValue()))
                            .toArray(Bson[]::new)
            );

            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(updateContext.getCollection());
            mongoCollection.updateMany(searchFilter, updates, new UpdateOptions().upsert(false));

            return OperationResult.Ok;
        } catch (MongoException mongoException) {
            throw new RuntimeException(mongoException);
        }
    }

    @Override
    public CompletableFuture<OperationResult> updateAsync(UpdateContext updateContext, PlaceholderContext placeholderContext) {
        return CompletableFuture.supplyAsync(() -> this.update(updateContext, placeholderContext), executor);
    }

    @Override
    public OperationResult insert(InsertContext updateContext, PlaceholderContext placeholderContext) {
        try {
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(updateContext.getCollection());

            Document document = new Document(
                    updateContext.getFields().getFormattedFields(placeholderContext)
            );

            mongoCollection.insertOne(document);

            return OperationResult.Ok;
        } catch (MongoException mongoException) {
            throw new RuntimeException(mongoException);
        }
    }

    @Override
    public CompletableFuture<OperationResult> insertAsync(InsertContext updateContext, PlaceholderContext placeholderContext) {
        return CompletableFuture.supplyAsync(() -> this.insert(updateContext, placeholderContext), executor);
    }

    @Override
    public OperationResult delete(DeleteContext updateContext, PlaceholderContext placeholderContext) {
        Bson searchFilter = BsonFilter.toBson(updateContext, placeholderContext);

        try {
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(updateContext.getCollection());

            mongoCollection.deleteMany(searchFilter);

            return OperationResult.Ok;
        } catch (MongoException mongoException) {
            throw new RuntimeException(mongoException);
        }
    }

    @Override
    public CompletableFuture<OperationResult> deleteAsync(DeleteContext updateContext, PlaceholderContext placeholderContext) {
        return CompletableFuture.supplyAsync(() -> this.delete(updateContext, placeholderContext), executor);
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public MongoDatabase getLegacySession() {
        return mongoDatabase;
    }

    @Override
    @Deprecated
    public void close() throws IOException {

    }
}
