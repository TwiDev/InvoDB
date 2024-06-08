package ch.twidev.invodb.bridge.exceptions;

public class DriverConfigException extends DriverConnectionException{

    public DriverConfigException() {
    }

    public DriverConfigException(String message) {
        super(message);
    }
}
