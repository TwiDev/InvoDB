package ch.twidev.invodb.bridge.driver.config;

public interface URLDriverConfig extends DriverConfig {

    String getUrl();

    @Override
    default boolean isUrl() {
        return true;
    }
}
