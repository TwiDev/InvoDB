package ch.twidev.invodb.bridge.driver.config;

import ch.twidev.invodb.bridge.driver.InvoDriverType;

public interface DriverConfigBuilder<Builder> {

    Builder setDriverType(InvoDriverType invoDriverType);

    DriverConfig build();

}
