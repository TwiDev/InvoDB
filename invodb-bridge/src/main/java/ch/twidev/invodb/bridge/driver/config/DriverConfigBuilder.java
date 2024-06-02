package ch.twidev.invodb.bridge.driver.config;

public interface DriverConfigBuilder<Builder, Config extends DriverConfig> {

    Builder setDriverName(String name);

    Config build();

}
