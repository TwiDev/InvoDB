package ch.twidev.invodb.driver.mongodb;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class MongoResultSet extends ElementSet<MongoCursor<Document>> {

    private final int capacity;

    public MongoResultSet(MongoCursor<Document> cursor) {
        super(StreamSupport.stream(Spliterators.spliteratorUnknownSize(cursor, 0), false)
                .map(MongoElements::new)
                .map(Elements.class::cast)
                .iterator(), cursor);

        this.capacity = cursor.available();
    }

    @Override
    public boolean isEmpty() {
        return capacity == 0;
    }

    @Override
    public Elements first() {
        return this.next();
    }

    @Override
    public ElementSet<MongoCursor<Document>> fromElements() {
        return new MongoResultSet(this.getElements());
    }

    @Override
    public long getTime() {
        return 0;
    }

    public static class MongoElements implements Elements {

        private final Document document;

        public MongoElements(Document document) {
            this.document = document;
        }

        @Override
        public boolean isValid() {
            return !document.isEmpty();
        }

        @Override
        public Object getObject(String name) {
            return document.get(name);
        }

        @Override
        public <T> T getObject(String name, Class<T> type) {
            return document.get(name,type);
        }

        @Override
        @Deprecated
        public <T> T getObject(int id, Class<T> type) {
            return null;
        }
    }
}
