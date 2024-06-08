package ch.twidev.invodb.driver.mongodb;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.operations.InsertContext;
import ch.twidev.invodb.bridge.operations.UpdateContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.scheduler.Scheduler;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.driver.mongodb.filter.BsonFilter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MongoConnection implements DriverSession<MongoDatabase> {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private final MongoDatabase mongoDatabase;

    public MongoConnection(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public ElementSet find(FindContext findOperationBuilder, PlaceholderContext placeholderContext) {
        Bson searchFilter = BsonFilter.toBson(findOperationBuilder);

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(findOperationBuilder.getCollection());
        FindIterable<Document> document = ((searchFilter != null) ? mongoCollection.find(searchFilter) : mongoCollection.find());

        return new MongoResultSet(document.iterator());
    }

    @Override
    public CompletableFuture<ElementSet> findAsync(FindContext findOperationBuilder, PlaceholderContext placeholderContext) {
        return CompletableFuture.supplyAsync(() -> this.find(findOperationBuilder, placeholderContext), executor);
    }

    @Override
    public OperationResult update(UpdateContext updateContext, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public CompletableFuture<OperationResult> updateAsync(UpdateContext updateContext, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public ElementSet insert(InsertContext updateContext, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public CompletableFuture<ElementSet> insertAsync(InsertContext updateContext, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public MongoDatabase getLegacySession() {
        return null;
    }

    @Override
    @Deprecated
    public void close() throws IOException {

    }
}
