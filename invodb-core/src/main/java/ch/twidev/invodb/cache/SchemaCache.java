package ch.twidev.invodb.cache;

import ch.twidev.invodb.bridge.cache.MapCache;
import ch.twidev.invodb.mapper.IndexedInvoSchema;

import java.lang.reflect.Field;

public interface SchemaCache<Schema extends IndexedInvoSchema<PrimaryKey>, PrimaryKey> extends MapCache<String> {

    Schema getSchema();

    void saveSchema(Schema schema);

}
