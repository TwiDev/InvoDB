package ch.twidev.invodb.drivers;

public interface DriverConnection<Driver> {

    Driver getDriver();

    boolean isConnected();

}
