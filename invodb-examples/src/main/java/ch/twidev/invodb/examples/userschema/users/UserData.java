package ch.twidev.invodb.examples.userschema.users;

public class UserData {

    private String key1 = "value1", key2 = "value2";
    private boolean settings = true;
    private int scope = 1;

    public UserData() {
    }

    public UserData(String key1, String key2, boolean settings, int scope) {
        this.key1 = key1;
        this.key2 = key2;
        this.settings = settings;
        this.scope = scope;
    }

    public String getKey1() {
        return key1;
    }

    public String getKey2() {
        return key2;
    }

    public boolean isSettings() {
        return settings;
    }

    public int getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "key1='" + key1 + '\'' +
                ", key2='" + key2 + '\'' +
                ", settings=" + settings +
                ", scope=" + scope +
                '}';
    }
}
