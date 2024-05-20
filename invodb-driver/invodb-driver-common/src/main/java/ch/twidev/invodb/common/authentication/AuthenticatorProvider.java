package ch.twidev.invodb.common.authentication;

public interface AuthenticatorProvider {

    default String getUsername() {
        return null;
    }

    default String getPassword() {
        return null;
    }

}
