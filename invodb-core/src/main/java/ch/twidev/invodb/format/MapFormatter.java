package ch.twidev.invodb.format;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapFormatter extends DataFormat<HashMap, String> {

    private final Gson gson;

    private final Type gsonType = new TypeToken(){}.getType();


    public MapFormatter() {
        super(HashMap.class);

        this.gson = new Gson();
    }

    @Override
    public String toPrimitive(HashMap hashMap) {
        return gson.toJson(hashMap, gsonType);
    }
}
