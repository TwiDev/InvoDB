package ch.twidev.invodb.bridge.driver.auth;

public interface AuthenticatorProvider {

    AuthenticatorProvider NONE = new PlainTextAuth("","");

}
