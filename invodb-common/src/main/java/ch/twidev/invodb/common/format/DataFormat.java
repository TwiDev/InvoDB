package ch.twidev.invodb.common.format;

import ch.twidev.invodb.common.format.annotations.StaticFormatter;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class DataFormat<Format, Primitive> {

    private static final String PACKAGE = "ch.twidev.invodb.common.format";

    private static final HashMap<Class<?>, DataFormat<?, ?>> staticFormatter = new HashMap<>();

    static {
        for (Class<? extends DataFormat> formatter : new Reflections(PACKAGE).getSubTypesOf(DataFormat.class)) {
            if (formatter.isAnnotationPresent(StaticFormatter.class)) {
                try {
                    DataFormat dataFormat = formatter.getConstructor().newInstance();

                    staticFormatter.put(dataFormat.getFormat(), dataFormat);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException ignored) {
                }
            }
        }
    }

    public static <F, P> Object getPrimitive(F format) {
        return getPrimitive(format, null);
    }

    @SuppressWarnings("unchecked")
    public static <F, P> Object getPrimitive(F format, Class<? extends DataFormat> dataFormat) {
        Class<F> formatInstance = (Class<F>) format.getClass();

        if (staticFormatter.containsKey(formatInstance)) {
            DataFormat<F,P> formatter = (DataFormat<F, P>) staticFormatter.get(formatInstance);

            return formatter.toPrimitive(format);
        }

        if(dataFormat == null) return null;

        if(dataFormat.isAssignableFrom(JsonFormatter.class)) {
            return new JsonFormatter<>(formatInstance).toPrimitive(format);
        }

        return null;
    }

    public static <F, P> F getFromPrimitive(P primitive, Class<F> format) {
        return getFromPrimitive(primitive, format, null);
    }


    @SuppressWarnings("unchecked")
    public static <F,P> F getFromPrimitive(P primitive, Class<F> format, Class<? extends DataFormat> dataFormat) {
        if(staticFormatter.containsKey(format)) {
            DataFormat<F,P> formatter = (DataFormat<F, P>) staticFormatter.get(format);

            return formatter.fromPrimitive(primitive);
        }

        if(dataFormat == null) return null;

        if(dataFormat.isAssignableFrom(JsonFormatter.class)) {
            return new JsonFormatter<>(format).fromPrimitive((String) primitive);
        }

        return null;
    }


    private final Class<Format> format;

    public DataFormat(Class<Format> format) {
        this.format = format;
    }

    public Class<Format> getFormat() {
        return format;
    }

    public abstract Primitive toPrimitive(Format format);

    public abstract Format fromPrimitive(Primitive format);
}
