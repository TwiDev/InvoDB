package ch.twidev.invodb.repository.handler;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.util.ResultCallback;
import ch.twidev.invodb.common.format.DataFormat;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.builder.InsertOperationBuilder;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.mapper.InvoSchema;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.mapper.annotations.Primitive;
import ch.twidev.invodb.repository.SchemaRepository;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import ch.twidev.invodb.repository.annotations.Find;
import ch.twidev.invodb.repository.annotations.Insert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public record SchemaRepositoryHandler<Session, Schema extends InvoSchema, Provider extends SchemaRepositoryProvider<Schema>>(
        SchemaRepository<Session, Schema, Provider> schemaRepository) implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CompletableFuture<Schema> schemaCompletableFuture = this.handleQuery(method, args);

        if(schemaCompletableFuture != null) {
            if(method.isAnnotationPresent(Async.class)) {
                return schemaCompletableFuture;
            }else {
                return schemaCompletableFuture.get();
            }
        }

        throw new UnsupportedOperationException("Unsupported method : " + method.getName());
    }

    public CompletableFuture<Schema> handleQuery(Method method, Object[] args) {
        if (method.isAnnotationPresent(Find.class)) {
            return handleFind(method, args);
        }

        if(method.isAnnotationPresent(Insert.class)) {
            return handleInsert(method, args);
        }

        return null;
    }

    public CompletableFuture<Schema> handleFind(Method method, Object[] args) {
        Find find = method.getAnnotation(Find.class);

        Object searchedValue = args[0];

        if(method.isAnnotationPresent(Primitive.class)) {
            Primitive primitive = method.getAnnotation(Primitive.class);

            searchedValue = DataFormat.getPrimitive(searchedValue, primitive.formatter());
        }

        CompletableFuture<Schema> schemaCompletableFuture = new CompletableFuture<>();

        // Todo: add query cache

        FindOperationBuilder findOperationBuilder = InvoQuery.find(schemaRepository.getCollection())
                .where(SearchFilter.eq(find.by(), searchedValue));

        CompletableFuture<ElementSet> completableFuture;

        if(method.isAnnotationPresent(Async.class)) {
            completableFuture = findOperationBuilder.runAsync(schemaRepository.getDriverSession());
        }else{
            completableFuture = CompletableFuture.completedFuture(
                    findOperationBuilder.run(schemaRepository.getDriverSession()));
        }

        completableFuture.exceptionally(throwable -> {
            schemaCompletableFuture.completeExceptionally(throwable);

            return null;
        }).thenAccept(elementSet -> {
            if(elementSet == null) return;

            try {
                Schema schema = schemaRepository.getSchema().getConstructor().newInstance();

                schema.populate(schemaRepository.getDriverSession(), schemaRepository.getCollection(), elementSet.first());

                schemaCompletableFuture.complete(schema);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {

                schemaCompletableFuture.completeExceptionally(e);
            }
        });

        return schemaCompletableFuture;
    }

    public CompletableFuture<Schema> handleInsert(Method method, Object[] args) {
        Insert insert = method.getAnnotation(Insert.class);

        if(args.length < insert.fields().length) {
            throw new IllegalArgumentException("There is no enough argument provided for the insert " + method.getName());
        }

        InsertOperationBuilder operationBuilder = InvoQuery.insert(schemaRepository.getCollection());

        // Todo: Add data format on query field?

        CompletableFuture<Schema> schemaCompletableFuture = new CompletableFuture<>();

        try {
            // Query
            Schema schema = schemaRepository.getSchema().getConstructor().newInstance();

            // Default Values 0,04 ms
            schema.load();

            // 0,5 ms
            schema.getFields().forEach((s, fieldMapper) -> {
                operationBuilder.field(s, fieldMapper.getFormattedValue());
            });

            for (int i = 0; i < insert.fields().length; i++) {
                String fieldName = insert.fields()[i];

                if(!schema.getFields().containsKey(fieldName)) continue;

                // Provide formatted value to the insert query
                Object value = schema.getFields().get(fieldName).getFormattedValue(args[i]);
                operationBuilder.field(fieldName, value);

                // Update value to the schema
                schema.getFields().get(fieldName).field().set(schema, value);
            }

            // Sync

            operationBuilder.run(schemaRepository.getDriverSession());
            schema.setExists(true);
            schema.setDriverSession(schemaRepository.getDriverSession());
            schema.setCollection(schemaRepository.getCollection());

            schemaCompletableFuture.complete(schema);


        } catch (Exception e) {
            schemaCompletableFuture.completeExceptionally(e);
        }

        return schemaCompletableFuture;
    }
}

