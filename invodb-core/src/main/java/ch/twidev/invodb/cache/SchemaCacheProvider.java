package ch.twidev.invodb.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.common.cache.MapCacheProvider;
import ch.twidev.invodb.mapper.IndexedInvoSchema;
import ch.twidev.invodb.mapper.annotations.Collection;
import ch.twidev.invodb.mapper.annotations.Uncacheable;
import ch.twidev.invodb.mapper.field.FieldMapper;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class SchemaCacheProvider<Driver, Schema extends IndexedInvoSchema<PrimaryKey>, PrimaryKey> extends MapCacheProvider<Driver> implements SchemaCache<Schema, PrimaryKey>{

    private final String collection;

    private final Class<Schema> schemaClass;

    public SchemaCacheProvider(CacheDriver<Driver> driver, Class<Schema> schemaClass, PrimaryKey primaryKey) {
        this(driver, null, schemaClass, primaryKey);
    }

    public SchemaCacheProvider(CacheDriver<Driver> driver, @Nullable SchemaCacheRepository<Driver> evictionPolicy, Class<Schema> schemaClass, PrimaryKey primaryKey) {
        super(driver, evictionPolicy, primaryKey.toString());

        if(!schemaClass.isAnnotationPresent(Collection.class)) {
            throw new IllegalArgumentException("The schema class must be in a declared collection");
        }

        this.collection = schemaClass.getAnnotation(Collection.class).name();

        this.schemaClass = schemaClass;
    }

    @Override
    public Schema getSchema() {
        try {
            Schema schema = schemaClass.getConstructor().newInstance();

            schema.load();

            Map<String, Object> map = this.getMap();
            schema.getFields().values().forEach(fieldMapper -> {
                final String cacheName = fieldMapper.cacheName();

                if(map.containsKey(cacheName)) {
                    try {
                        fieldMapper.field().set(schema, fieldMapper.getFormattedValue(map.get(cacheName)));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            schema.setCollection(collection);
            schema.setExists(true);
            schema.setCached(true);

            schema.onPopulated();

            return schema;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveSchema(Schema schema) {
        if(!schema.exists()) {
            throw new IllegalArgumentException("Cannot cache a non populated schema");
        }

        for (FieldMapper fieldMapper : schema.getFields().values()) {
            if(fieldMapper.field().isAnnotationPresent(Uncacheable.class)) {
                continue;
            }

            this.put(fieldMapper.cacheName(), fieldMapper.getFormattedValue());
        }
    }

    public Object get(Field field) {
        return null;
    }

}
