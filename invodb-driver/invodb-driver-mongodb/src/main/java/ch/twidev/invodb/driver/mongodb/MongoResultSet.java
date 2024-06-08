package ch.twidev.invodb.driver.mongodb;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import com.google.common.collect.Streams;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.Iterator;

public class MongoResultSet implements ElementSet {

    private final int capacity;
    private final Iterator<MongoElements> parsedElements;

    public MongoResultSet(MongoCursor<Document> cursor) {
        this.capacity = cursor.available();

        parsedElements = Streams.stream(cursor)
                .map(MongoElements::new)
                .iterator();

        cursor.close();
    }

    @Override
    public boolean isEmpty() {
        return capacity != 0;
    }

    @Override
    public Elements first() {
        return parsedElements.next();
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public boolean hasNext() {
        return parsedElements.hasNext();
    }

    @Override
    public Elements next() {
        return parsedElements.next();
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
            return null;
        }

        @Override
        public <T> T getObject(String name, Class<T> type) {
            return null;
        }

        @Override
        public <T> T getObject(int id, Class<T> type) {
            return null;
        }
    }
}
