package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.util.Iterator;

public class ScyllaResultSet implements ElementSet {

    private final ResultSet resultSet;
    private final Iterator<Elements> parsedElements;

    public ScyllaResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;

        this.parsedElements = resultSet.all()
                .stream()
                .map(ScyllaElements::new)
                .map(Elements.class::cast)
                .iterator();
    }

    @Override
    public Iterator<Elements> iterator() {
        return parsedElements;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public long getTime() {
        return resultSet.getExecutionInfo().getQueryTrace().getDurationMicros();
    }

    public static class ScyllaElements implements Elements {

        private final Row row;

        public ScyllaElements(Row row) {
            this.row = row;
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
