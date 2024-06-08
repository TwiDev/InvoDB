package ch.twidev.invodb.exception;

public class InvalidRepositoryQueryException extends SchemaException{

    public InvalidRepositoryQueryException(String message) {
        super(message);
    }

    public InvalidRepositoryQueryException(Throwable cause) {
        super(cause);
    }
}
