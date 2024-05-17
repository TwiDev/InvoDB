package ch.twidev.invodb.drivers;

import java.util.HashMap;

public class DriverConfig {

    private DriverType driverType;

    private HashMap<EnvVar, Object> configVars = new HashMap<>();

}
