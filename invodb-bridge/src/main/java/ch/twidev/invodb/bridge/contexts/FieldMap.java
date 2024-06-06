package ch.twidev.invodb.bridge.contexts;

import java.util.HashMap;

public class FieldMap extends HashMap<String, Object> {

    // Todo : Exception manager


    @Override
    public String toString() {
        return String.join("= ? ," + this.keySet());
    }
}
