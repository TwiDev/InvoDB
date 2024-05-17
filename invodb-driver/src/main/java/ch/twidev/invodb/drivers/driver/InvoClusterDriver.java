package ch.twidev.invodb.drivers.driver;

import ch.twidev.invodb.drivers.InvoDriver;
import ch.twidev.invodb.drivers.DriverConfig;
import ch.twidev.invodb.drivers.DriverType;
import ch.twidev.invodb.exceptions.DriverConnectionException;
import com.datastax.driver.core.Cluster;

public class InvoClusterDriver extends InvoDriver<Cluster> {

    private static final String CLUSTER_DRIVER_KEY = "ScyllaClusterDriver";


    public InvoClusterDriver(DriverConfig driverConfig) {
        super(driverConfig, DriverType.SCYLLA, CLUSTER_DRIVER_KEY);
    }

    @Override
    public void initConnection() throws DriverConnectionException {
        Cluster cluster = Cluster.builder()
                .withClusterName(CLUSTER_DRIVER_KEY)
                .addContactPoint(
                        "driverConfig"
                )
                .withPort(0)
                .build();

        this.setConnection(cluster);
    }

    @Override
    public void closeConnection() {

    }

    @Override
    public boolean isConnected() {
        return this.getConnection() != null;
    }
}
