package ch.twidev.invodb.repository.handler;

import ch.twidev.invodb.common.format.DataFormat;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.mapper.InvoSchema;
import ch.twidev.invodb.mapper.annotations.Primitive;
import ch.twidev.invodb.repository.SchemaRepository;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import ch.twidev.invodb.repository.annotations.Find;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public record SchemaRepositoryHandler<Session, Schema extends InvoSchema, Provider extends SchemaRepositoryProvider<Schema>>(
        SchemaRepository<Session, Schema, Provider> schemaRepository) implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        CompletableFuture<Schema> schema = this.parseSchema(method, args);
        if(schema != null){
            return schema;
        }

        throw new UnsupportedOperationException("Unsupported method : " + method.getName());
    }

    public CompletableFuture<Schema> parseSchema(Method method, Object[] args) {
        if(args.length == 0) return null;

        if (method.isAnnotationPresent(Find.class)) {
            Find find = method.getAnnotation(Find.class);

            Object searchedValue = args[0];

            if(method.isAnnotationPresent(Primitive.class)) {
                Primitive primitive = method.getAnnotation(Primitive.class);

                searchedValue = DataFormat.getPrimitive(searchedValue, primitive.formatter());
            }

            CompletableFuture<Schema> schemaCompletableFuture = new CompletableFuture<>();

            InvoQuery.find(schemaRepository.getCollection())
                    .where(SearchFilter.eq(find.by(), searchedValue))
                    .run(schemaRepository.getDriverSession(), elementSet -> {
                        try {
                            // Query
                            Schema schema = schemaRepository.getSchema().getConstructor().newInstance();

                            schema.populate(schemaRepository.getDriverSession(), schemaRepository.getCollection(), elementSet.first());

                            schemaCompletableFuture.complete(schema);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

            return schemaCompletableFuture;
        }

        return null;
    }
}

