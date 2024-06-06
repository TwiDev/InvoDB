package ch.twidev.invodb.mapper;

import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.session.DriverSession;

import java.lang.reflect.Field;

public abstract class InvoSchema {

    private final String collection;

    private boolean exists = false;

    private DriverSession<?> driverSession = null;

    public InvoSchema(String collection) {
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

    public boolean isExists() {
        return exists && driverSession.isConnected();
    }

    public void populate(DriverSession<?> driverSession, Elements elements) {
        this.driverSession = driverSession;

        try {
            for (Field declaredField : this.getClass().getDeclaredFields()) {
                ch.twidev.invodb.mapper.annotations.Field annotation = declaredField.getAnnotation(ch.twidev.invodb.mapper.annotations.Field.class);

                if (annotation != null) {
                    declaredField.setAccessible(true);
                    String fieldName = annotation.name().isEmpty() ? declaredField.getName() : annotation.name();

                    declaredField.set(
                            this, elements.getObject(fieldName, declaredField.getType())
                    );

                    /**
                     * Todo: Add data format
                     */
                }
            }

            this.exists = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public DriverSession<?> getDriverSession() {
        return driverSession;
    }

    public void setDriverSession(DriverSession<?> driverSession) {
        this.driverSession = driverSession;
    }
}
