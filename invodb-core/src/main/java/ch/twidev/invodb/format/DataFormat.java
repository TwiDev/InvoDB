package ch.twidev.invodb.format;

public abstract class DataFormat<Format, Primitive> {

    private final Class<Format> format;

    public DataFormat(Class<Format> format) {
        this.format = format;
    }

    public abstract Primitive toPrimitive(Format format);
}
