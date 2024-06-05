package ch.twidev.invodb.bridge.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PlaceholderContext {

    public static PlaceholderContext empty() {
        return new PlaceholderContext();
    }

    public static PlaceholderContext from(QueryPlaceholder k1, Object v1) {
        return new PlaceholderContext().set(k1, v1);
    }

    public static PlaceholderContext from(String k1, Object v1) {
        return new PlaceholderContext().set(k1, v1);
    }

    public static PlaceholderContext from(QueryPlaceholder k1, Object v1, QueryPlaceholder k2, Object v2) {
        return new PlaceholderContext()
                .set(k1, v1)
                .set(k2, v2);
    }

    public static PlaceholderContext from(String k1, Object v1, String k2, Object v2) {
        return new PlaceholderContext()
                .set(k1,v1)
                .set(k2,v2);
    }

    public static PlaceholderContext from(QueryPlaceholder k1, Object v1, QueryPlaceholder k2, Object v2, QueryPlaceholder k3, Object v3) {
        return new PlaceholderContext()
                .set(k1, v1)
                .set(k2, v2)
                .set(k3, v3);
    }

    public static PlaceholderContext from(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return new PlaceholderContext()
                .set(k1,v1)
                .set(k2,v2)
                .set(k3,v3);
    }

    private final HashMap<String, Object> context;

    public PlaceholderContext() {
        this.context = new HashMap<>();
    }

    public PlaceholderContext(HashMap<QueryPlaceholder, Object> context) {
        this.context = context.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> "{%s}".formatted(entry.getKey().getPlaceholder()),
                        Map.Entry::getValue,
                        (oldValue, newValue) -> newValue,
                        HashMap::new
                ));
    }

    public PlaceholderContext set(QueryPlaceholder queryPlaceholder, Object value) {
        context.put(queryPlaceholder.getPlaceholder(), value);

        return this;
    }

    public PlaceholderContext set(String queryPlaceholder, Object value) {
        context.put(queryPlaceholder, value);

        return this;
    }

    public Object get(QueryPlaceholder queryPlaceholder) {
        return context.getOrDefault(queryPlaceholder.getPlaceholder(), null);
    }

    public Object get(String placeHolder) {
        return context.getOrDefault(placeHolder, null);
    }


    public HashMap<String, Object> getContext() {
        return context;
    }
}
