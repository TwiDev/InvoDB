package ch.twidev.invodb.bridge.contexts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Attributes extends ArrayList<String> {

    public static Attributes ALL = new Attributes(Collections.singletonList("*"));

    public Attributes(List<String> list) {
        super(list);
    }

    public Attributes() {
        super();
    }

    @Override
    public String toString() {
        if(this.size() == 1) {
            return this.get(0);
        }else{
            return String.join(",", this);
        }
    }
}
