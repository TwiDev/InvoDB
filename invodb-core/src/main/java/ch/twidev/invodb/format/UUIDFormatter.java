package ch.twidev.invodb.format;

import java.util.UUID;

public class UUIDFormatter extends DataFormat<UUID, String> {


    public UUIDFormatter() {
        super(UUID.class);
    }

    @Override
    public String toPrimitive(UUID uuid) {
        return uuid.toString();
    }
}
