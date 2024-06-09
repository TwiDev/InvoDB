package ch.twidev.invodb.bridge.contexts;

import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldMap extends HashMap<String, Object> {

    // Todo : Exception manager

    public FieldMap getFormattedFields(PlaceholderContext placeholderContext) {
        if(placeholderContext == null || placeholderContext.getContext().isEmpty()) {
            return this;
        }

        return new FieldMap(this.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        object -> {
                            if(object.getValue() instanceof QueryPlaceholder placeholder) {
                                return placeholderContext.get(placeholder);
                            }

                            return object.getValue();
                        }
                )));
    }

    public FieldMap() {
    }

    public FieldMap(Map<? extends String, ?> m) {
        super(m);
    }

    @Override
    public String toString() {
        return String.join("= ? ,", this.keySet()) + " = ?";
    }

    public String getKeysString() {
        return String.join(", ", this.keySet());
    }

    public String getValuesString() {
        return String.join(", ", this.values()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet())
        );
    }

    public String getUnbounded() {
        return String.join(", ", Collections.nCopies(this.size(), "?"));
    }
}
