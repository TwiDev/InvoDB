package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.driver.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.auth.AuthenticatorProvider;
import ch.twidev.invodb.bridge.driver.cluster.ClusterDriver;
import ch.twidev.invodb.bridge.driver.cluster.ClusterPoint;
import ch.twidev.invodb.bridge.driver.cluster.ClusterPoints;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigMissingException;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.session.DriverSession;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.concurrent.CompletableFuture;

public class ScyllaDriver extends ClusterDriver<Session, DriverSession<Session>> {

    private Cluster cluster;

    private static final String CLUSTER_DRIVER_KEY = "ScyllaClusterDriver";

    public ScyllaDriver(DriverConfig driverConfig) {
        super(driverConfig, InvoDriverType.SCYLLA);
    }

    @Override
    public void initDriver() throws DriverConnectionException {
        DriverConfig driverConfig = this.getDriverConfig();

        if(driverConfig.isUrl()) {
            return;
        }

        if(!(driverConfig.get(EnvVar.SCYLLA_HOSTS) instanceof ClusterPoints clusterPoints))
            throw new DriverConfigMissingException(EnvVar.SCYLLA_HOSTS, driverConfig);

        if(!(driverConfig.get(EnvVar.SCYLLA_AUTHENTICATOR) instanceof AuthenticatorProvider authenticator))
            throw new DriverConfigMissingException(EnvVar.SCYLLA_AUTHENTICATOR, driverConfig);


        Cluster.Builder clusterBuilder = Cluster.builder()
                .withClusterName(CLUSTER_DRIVER_KEY)
                .addContactPointsWithPorts(
                        clusterPoints.stream()
                                .map(ClusterPoint::inetSocketAddress)
                                .toList());

        if(authenticator instanceof NoneAuthenticator) {
            clusterBuilder.withAuthProvider(AuthProvider.NONE);
        }else{
            clusterBuilder.withAuthProvider(new PlainTextAuthProvider(authenticator.getUsername(), authenticator.getPassword()));
        }

        this.cluster = clusterBuilder.build();
    }

    @Override
    public boolean exists() {
        return cluster != null && !isClosed();
    }

    @Override
    public DriverSession<Session> connectSession(String keyname) {
        return null;
    }

    @Override
    public CompletableFuture<DriverSession<Session>> asyncConnectSession(String keyname) {
        return null;
    }

    @Override
    public void close() {
        cluster.close();
    }

    @Override
    public boolean isClosed() {
        return cluster.isClosed();
    }
}
