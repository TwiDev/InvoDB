package ch.twidev.invodb.mapper;

import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.format.DataFormat;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.DeleteOperationBuilder;
import ch.twidev.invodb.common.query.builder.UpdateOperationBuilder;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.exception.SchemaException;
import ch.twidev.invodb.exception.runtime.PopulateSchemaException;
import ch.twidev.invodb.exception.runtime.QuerySchemaException;
import ch.twidev.invodb.mapper.annotations.Primitive;
import ch.twidev.invodb.mapper.field.FieldMapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public abstract class InvoSchema {

    private final HashMap<String, FieldMapper> fields = new HashMap<>();

    private String collection = null;

    private boolean exists = false;

    private DriverSession<?> driverSession = null;
    public String getCollection() {
        return collection;
    }

    public boolean isExists() {
        return exists && driverSession.isConnected() && collection != null;
    }

    public void load() {
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            ch.twidev.invodb.mapper.annotations.Field annotation = declaredField.getAnnotation(ch.twidev.invodb.mapper.annotations.Field.class);

            if (annotation != null) {
                declaredField.setAccessible(true);
                String fieldName = annotation.name().isEmpty() ? declaredField.getName() : annotation.name();

                final Primitive primitive;

                if(declaredField.isAnnotationPresent(Primitive.class)) {
                    primitive = declaredField.getAnnotation(Primitive.class);
                }else{
                    primitive = null;
                }

                fields.put(declaredField.getName(),
                        new FieldMapper(this, declaredField.getName(), fieldName, primitive, declaredField));
            }
        }
    }

    public void populate(DriverSession<?> driverSession, String collection, Elements elements) {
        this.driverSession = driverSession;
        this.collection = collection;

        if(fields.isEmpty())
            this.load();

        fields.forEach((s, fieldMapper) -> {
            try {
                final Object object;

                if (fieldMapper.hasFormatter()) {
                    object = DataFormat.getFromPrimitive(
                            elements.getObject(fieldMapper.queryName()),
                            fieldMapper.field().getType(),
                            fieldMapper.primitive().formatter()
                    );
                } else {
                    object = elements.getObject(fieldMapper.queryName(), fieldMapper.field().getType());
                }

                fieldMapper.field().set(
                        this, object
                );
            } catch (Exception e) {
                e.printStackTrace();

                throw new PopulateSchemaException(e);
            }
        });

        this.exists = true;
    }

    public void save(SearchFilter searchFilter) {
        this.saveQuery(searchFilter).run(driverSession);
    }

    public CompletableFuture<OperationResult> saveAsync(SearchFilter searchFilter) {
        return this.saveQuery(searchFilter).runAsync(driverSession);
    }

    public void delete(SearchFilter searchFilter){
        this.deleteQuery(searchFilter).run(driverSession);
    }

    public CompletableFuture<OperationResult> deleteAsync(SearchFilter searchFilter){
        return this.deleteQuery(searchFilter).runAsync(driverSession);
    }

    private UpdateOperationBuilder saveQuery(SearchFilter searchFilter) {
        if(!this.isExists()) {
            throw new QuerySchemaException("Schema isn't populate cannot save it");
        }

        UpdateOperationBuilder operationBuilder = InvoQuery.update(this.getCollection())
                .where(searchFilter);

        this.getFields().forEach((s, fieldMapper) -> {
            operationBuilder.field(s, fieldMapper.getFormattedValue());
        });

        return operationBuilder;
    }

    private DeleteOperationBuilder deleteQuery(SearchFilter searchFilter) {
        if(!this.isExists()) {
            throw new QuerySchemaException("Schema isn't populate cannot save it");
        }

        return InvoQuery.delete(collection)
                .where(searchFilter);
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public DriverSession<?> getDriverSession() {
        return driverSession;
    }

    public void setDriverSession(DriverSession<?> driverSession) {
        this.driverSession = driverSession;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public HashMap<String, FieldMapper> getFields() {
        return fields;
    }
}
