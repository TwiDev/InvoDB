package ch.twidev.invodb.bridge.driver.config;

import ch.twidev.invodb.bridge.driver.auth.AuthenticatorProvider;

public interface DriverConfigBuilder<Builder, Config extends DriverConfig> {

    Builder setAuthProvider(AuthenticatorProvider authenticatorProvider);

    Builder setDriverName(String name);

    Config build();

}
