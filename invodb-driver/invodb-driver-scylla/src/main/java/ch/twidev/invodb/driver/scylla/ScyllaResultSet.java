package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class ScyllaResultSet extends ElementSet<ResultSet> {

    private final boolean isEmpty;
    private final transient ResultSet resultSet;
    private final transient Elements first;

    public ScyllaResultSet(ResultSet resultSet) {
        super(resultSet.all()
                .stream()
                .map(ScyllaElements::new)
                .map(Elements.class::cast)
                .iterator(), resultSet);

        this.resultSet = resultSet;

        this.isEmpty = resultSet.isExhausted();

        this.first = new ScyllaElements(resultSet.one());
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public long getTime() {
        return resultSet.getExecutionInfo().getQueryTrace().getDurationMicros();
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public Elements first() {
        return first;
    }

    @Override
    public ElementSet<ResultSet> fromElements() {
        return new ScyllaResultSet(this.getElements());
    }

    public static class ScyllaElements implements Elements {

        private final transient Row row;

        public ScyllaElements(Row row) {
            this.row = row;
        }

        @Override
        public boolean isValid() {
            return row != null;
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
