package ch.twidev.invodb.format;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapFormatter extends DataFormat<Map, String> {

    private final Gson gson;



    public MapFormatter() {
        super(Map.class);

        this.gson = new Gson();
    }

    @Override
    public String toPrimitive(Map hashMap) {
        return gson.toJson(hashMap);
    }
}
