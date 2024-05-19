package ch.twidev.invodb.authentication;

public record Authenticator(String username, String password) implements AuthenticatorProvider {
    @Override
    public String getUsername() {
        return this.username();
    }

    @Override
    public String getPassword() {
        return this.password();
    }
}
