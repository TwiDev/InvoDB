package ch.twidev.invodb.bridge.contexts;

import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FieldMap extends HashMap<String, Object> {

    // Todo : Exception manager


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
