package ch.twidev.invodb.common.drivers;

public interface DriverConnection<Driver> {

    Driver getDriver();

    boolean isConnected();

}
