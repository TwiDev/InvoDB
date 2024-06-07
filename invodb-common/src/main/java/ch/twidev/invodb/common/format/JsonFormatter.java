package ch.twidev.invodb.common.format;

import com.google.gson.Gson;

public class JsonFormatter<E> extends DataFormat<E, String> {

    private final Gson gson;

    public JsonFormatter(Class<E> eClass) {
        super(eClass);

        this.gson = new Gson();
    }

    @Override
    public String toPrimitive(E hashMap) {
        return gson.toJson(hashMap);
    }

    @Override
    public E fromPrimitive(String format) {
        return gson.fromJson(format, this.getFormat());
    }
}
