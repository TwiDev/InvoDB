package ch.twidev.invodb.cache;

import ch.twidev.invodb.bridge.cache.MapCache;
import ch.twidev.invodb.mapper.IndexedInvoSchema;

public interface SchemaCache<Schema extends IndexedInvoSchema> extends MapCache<String> {

    Schema getSchema();

    void saveSchema(Schema schema);

}
