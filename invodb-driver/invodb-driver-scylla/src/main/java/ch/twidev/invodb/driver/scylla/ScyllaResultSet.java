package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.util.Iterator;

public class ScyllaResultSet implements ElementSet {

    private final boolean isEmpty;
    private final ResultSet resultSet;
    private final Elements first;
    private final Iterator<Elements> parsedElements;

    public ScyllaResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;

        this.isEmpty = resultSet.isExhausted();
        this.first = new ScyllaElements(resultSet.one());

        this.parsedElements = resultSet.all()
                .stream()
                .map(ScyllaElements::new)
                .map(Elements.class::cast)
                .iterator();

    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public long getTime() {
        return resultSet.getExecutionInfo().getQueryTrace().getDurationMicros();
    }

    @Override
    public boolean hasNext() {
        return parsedElements.hasNext();
    }

    @Override
    public Elements next() {
        return parsedElements.next();
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public Elements first() {
        return first;
    }

    public static class ScyllaElements implements Elements {

        private final Row row;

        public ScyllaElements(Row row) {
            this.row = row;
        }

        @Override
        public Object getObject(String name) {
            return row.getObject(name);
        }

        @Override
        public <T> T getObject(String name, Class<T> type) {
            return row.get(name, type);
        }

        @Override
        public <T> T getObject(int id, Class<T> type) {
            return row.get(id, type);
        }
    }

}
