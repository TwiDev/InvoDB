package ch.twidev.invodb.driver.mongodb;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import com.google.common.collect.Streams;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class MongoResultSet implements ElementSet {

    private final int capacity;
    private final Iterator<MongoElements> parsedElements;


    public MongoResultSet(MongoCursor<Document> cursor) {
        this.capacity = cursor.available();

        parsedElements = StreamSupport.stream(Spliterators.spliteratorUnknownSize(cursor, 0), false)
                .map(MongoElements::new)
                .iterator();
    }

    @Override
    public boolean isEmpty() {
        return capacity == 0;
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
