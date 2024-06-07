package ch.twidev.invodb.common.format;

import ch.twidev.invodb.common.format.annotations.StaticFormatter;

import java.util.UUID;

@StaticFormatter
public class UUIDFormatter extends DataFormat<UUID, String> {

    public UUIDFormatter() {
        super(UUID.class);
    }

    @Override
    public String toPrimitive(UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID fromPrimitive(String format) {
        return UUID.fromString(format);
    }
}
