package ch.twidev.invodb.authentication;

public interface AuthenticatorProvider {

    default String getUsername() {
        return null;
    }

    default String getPassword() {
        return null;
    }

}
