package ch.twidev.invodb.mapper;

import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.format.DataFormat;
import ch.twidev.invodb.mapper.annotations.Primitive;
import ch.twidev.invodb.mapper.field.FieldMapper;

import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class InvoSchema {

    private final HashMap<String, FieldMapper> fields = new HashMap<>();

    private String collection = null;

    private boolean exists = false;

    private DriverSession<?> driverSession = null;
    public String getCollection() {
        return collection;
    }

    public boolean isExists() {
        return exists && driverSession.isConnected() && collection != null;
    }

    public InvoSchema populate(DriverSession<?> driverSession, String collection, Elements elements) {
        this.driverSession = driverSession;
        this.collection = collection;

        try {
            for (Field declaredField : this.getClass().getDeclaredFields()) {
                ch.twidev.invodb.mapper.annotations.Field annotation = declaredField.getAnnotation(ch.twidev.invodb.mapper.annotations.Field.class);

                if (annotation != null) {
                    declaredField.setAccessible(true);
                    String fieldName = annotation.name().isEmpty() ? declaredField.getName() : annotation.name();

                    final Primitive primitive;
                    final Object object;

                    if(declaredField.isAnnotationPresent(Primitive.class)) {
                        primitive = declaredField.getAnnotation(Primitive.class);

                        object = DataFormat.getFromPrimitive(
                                elements.getObject(fieldName),
                                declaredField.getType(),
                                primitive.formatter()
                        );
                    }else{
                        primitive = null;

                        object = elements.getObject(fieldName, declaredField.getType());
                    }

                    declaredField.set(
                            this, object
                    );

                    fields.put(declaredField.getName(),
                            new FieldMapper(this, declaredField.getName(), fieldName, primitive, declaredField));
                }
            }

            this.exists = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this;
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

    public HashMap<String, FieldMapper> getFields() {
        return fields;
    }
}
