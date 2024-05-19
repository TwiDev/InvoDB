package ch.twidev.invodb.exceptions;

public class DriverBuilderException extends Exception {

    public DriverBuilderException(String fieldMissing) {
        super(String.format("%s field is missing or undefined in the builder, cannot build the object.", fieldMissing));
    }
}
