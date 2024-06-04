package ch.twidev.invodb.bridge.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PlaceholderContext {

    public static PlaceholderContext empty() {
        return new PlaceholderContext();
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
