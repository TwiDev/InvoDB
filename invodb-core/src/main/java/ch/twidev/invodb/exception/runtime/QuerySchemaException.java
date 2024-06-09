package ch.twidev.invodb.exception.runtime;

public class QuerySchemaException extends RuntimeException {

    public QuerySchemaException() {
    }

    public QuerySchemaException(String message) {
        super(message);
    }

    public QuerySchemaException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuerySchemaException(Throwable cause) {
        super(cause);
    }

    public QuerySchemaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
