package ch.twidev.invodb.exceptions;

public class DriverException extends Exception{

    public DriverException() {
    }

    public DriverException(String message) {
        super(message);
    }

    public DriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverException(Throwable cause) {
        super(cause);
    }

    public DriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
