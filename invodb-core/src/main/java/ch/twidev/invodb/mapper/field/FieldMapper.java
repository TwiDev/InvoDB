package ch.twidev.invodb.mapper.field;

import ch.twidev.invodb.common.format.DataFormat;
import ch.twidev.invodb.mapper.InvoSchema;
import ch.twidev.invodb.mapper.annotations.Primitive;

import java.lang.reflect.Field;

public record FieldMapper(InvoSchema invoSchema, String name, String queryName, String cacheName, Primitive primitive, Field field) {

    public boolean hasFormatter() {
        return primitive != null;
    }

    public Object getFormattedValue() {
        try {
            Object real = field.get(invoSchema);

            if(hasFormatter()) {
                return DataFormat.getPrimitive(real, primitive.formatter());
            }

            return real;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getFormattedValue(Object value) {
        if(hasFormatter()) {
            return DataFormat.getPrimitive(value, primitive.formatter());
        }

        return value;
    }
}
