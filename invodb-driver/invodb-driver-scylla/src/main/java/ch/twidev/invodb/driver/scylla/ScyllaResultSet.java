package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.ElementSetWrapper;
import ch.twidev.invodb.bridge.documents.Elements;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.io.*;
import java.util.*;

public class ScyllaResultSet extends ElementSet<ScyllaResultSet.ScyllaElements> {

    @Serial
    private static final long serialVersionUID = 23791447298645043L;

    private final boolean isEmpty;
    private final transient ResultSet resultSet;
    private transient Elements first;

    public ScyllaResultSet(ResultSet resultSet) {
        super(resultSet.all()
                .stream()
                .map(ScyllaElements::new)
                .iterator(), ScyllaResultWrapper.class);

        this.resultSet = resultSet;

        this.isEmpty = resultSet.isExhausted();
        System.out.println(resultSet.all().toString());

        try {
            this.first = new ScyllaElements(resultSet.one());
        } catch (NoSuchElementException ignore) {
            this.first = null;
        }
    }

    public ScyllaResultSet(Iterator<ScyllaElements> iterator) {
        super(iterator, ScyllaResultWrapper.class);

        this.isEmpty = iterator.hasNext();
        this.resultSet = null;
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
    public ElementSet<ScyllaElements> fromElements() {
        return new ScyllaResultSet(this.getWrapper().getElements());
    }

    public static class ScyllaResultWrapper extends ElementSetWrapper<ScyllaElements> implements Serializable {

        public ScyllaResultWrapper(Iterator<ScyllaElements> resultSet) {
            super(resultSet);
        }

        @Serial
        private void writeObject(ObjectOutputStream oos) {
            try {
                while (elements.hasNext()) {
                    oos.writeObject(new ScyllaRowWrapper(elements.next().getRow()));
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        @Serial
        private void readObject(ObjectInputStream ois) {
            try {
                final LinkedList<ScyllaElements> rows = new LinkedList<>();

                while (true) {
                    try {
                        Object obj = ois.readObject();
                        System.out.println(obj.toString());

                        if (obj instanceof ScyllaElements row) {
                            rows.add(row);
                        }
                    } catch (Exception oef) {
                        break;
                    }
                }

                System.out.println(rows.toString());


                elements = rows.iterator();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ScyllaRowWrapper extends ScyllaElements implements Serializable {

        @Serial
        private static final long serialVersionUID = 1233453454L;

        private Map<String, Object> data = new HashMap<>();

        public ScyllaRowWrapper(Row row) {
            super(row);

            for (ColumnDefinitions.Definition definition : row.getColumnDefinitions()) {
                String columnName = definition.getName();
                Object columnValue = row.getObject(columnName);

                data.put(columnName, columnValue);
            }
        }

        @Serial
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(data);
        }

        @Serial
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            try {
                data = (Map<String, Object>) in.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public Map<String, Object> getData() {
            return data;
        }

        @Override
        public boolean isValid() {
            return !data.isEmpty();
        }

        @Override
        public Object getObject(String name) {
            return data.get(name);
        }

        @Override
        public <T> T getObject(String name, Class<T> type) {
            return type.cast(data.get(name));
        }

        @Override
        public <T> T getObject(int id, Class<T> type) {
            return null;
        }
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

        public Row getRow() {
            return row;
        }
    }

}
