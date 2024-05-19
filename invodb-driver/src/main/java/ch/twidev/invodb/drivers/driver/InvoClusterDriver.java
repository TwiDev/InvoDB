package ch.twidev.invodb.drivers.driver;

import ch.twidev.invodb.authentication.AuthenticatorProvider;
import ch.twidev.invodb.authentication.NoneAuthenticator;
import ch.twidev.invodb.cluster.ClusterPoint;
import ch.twidev.invodb.cluster.ClusterPoints;
import ch.twidev.invodb.drivers.DriverConfig;
import ch.twidev.invodb.drivers.DriverType;
import ch.twidev.invodb.drivers.InvoDriver;
import ch.twidev.invodb.environment.EnvVar;
import ch.twidev.invodb.exceptions.DriverConfigMissingException;
import ch.twidev.invodb.exceptions.DriverConnectionException;
import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class InvoClusterDriver extends InvoDriver<Cluster> {

    private static final String CLUSTER_DRIVER_KEY = "ScyllaClusterDriver";


    public InvoClusterDriver(DriverConfig driverConfig) {
        super(driverConfig, DriverType.SCYLLA, CLUSTER_DRIVER_KEY);
    }

    @Override
    public void initConnection() throws DriverConnectionException {
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

        Cluster cluster = clusterBuilder.build();

        this.setConnection(cluster);
    }


    public CompletableFuture<InvoSessionDriver> asyncConnect(String keyspace) {
        CompletableFuture<InvoSessionDriver> invoSessionDriverFutureCallback = new CompletableFuture<>();

        ListenableFuture<Session> asyncTask = this.getConnection().connectAsync(keyspace);
        Futures.addCallback(asyncTask, new com.google.common.util.concurrent.FutureCallback<>() {
            @Override
            public void onSuccess(@Nullable Session session) {
                invoSessionDriverFutureCallback.complete(
                        InvoSessionDriver.fromScyllaSession(session)
                );
            }

            @Override
            public void onFailure(Throwable throwable) {
                invoSessionDriverFutureCallback.completeExceptionally(throwable);
            }
        });

        return invoSessionDriverFutureCallback;
    };

    @Override
    public void closeConnection() {
        this.getConnection().close();
    }

    @Override
    public boolean isConnected() {
        return this.getConnection() != null && !this.getConnection().isClosed();
    }
}
