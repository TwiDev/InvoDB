package ch.twidev.invodb.bridge.driver.auth;

public record PlainTextAuth(String username, String password) implements AuthenticatorProvider{

    public boolean isEmpty() {
        return username.isEmpty() || password.isEmpty();
    }

}
