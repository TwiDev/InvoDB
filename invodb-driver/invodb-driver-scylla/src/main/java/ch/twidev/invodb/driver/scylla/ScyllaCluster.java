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
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class ScyllaCluster extends ClusterDriver<Session, ScyllaConnection> {

    private Cluster cluster;

    private static final String CLUSTER_DRIVER_KEY = "ScyllaClusterDriver";

    public ScyllaCluster(DriverConfig driverConfig) {
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

        /*if(authenticator instanceof NoneAuthenticator) {
            clusterBuilder.withAuthProvider(AuthProvider.NONE);
        }else{
            clusterBuilder.withAuthProvider(new PlainTextAuthProvider(authenticator.getUsername(), authenticator.getPassword()));
        }*/

        this.cluster = clusterBuilder.build();
    }

    @Override
    public boolean exists() {
        return cluster != null && !isClosed();
    }

    @Override
    public ScyllaConnection connectSession(String keyname) {
        return null;
    }

    @Override
    public CompletableFuture<ScyllaConnection> asyncConnectSession(String keyname) {
        CompletableFuture<ScyllaConnection> invoSessionDriverFutureCallback = new CompletableFuture<>();

        ListenableFuture<Session> asyncTask = this.cluster.connectAsync(keyname);

        Futures.addCallback(asyncTask, new FutureCallback<>() {
            @Override
            public void onSuccess(Session session) {
                System.out.println("hello");

                invoSessionDriverFutureCallback.complete(
                        new ScyllaConnection(session)
                );
            }

            @Override
            public void onFailure(@NotNull Throwable throwable) {
                invoSessionDriverFutureCallback.completeExceptionally(throwable);
            }
        }, Executors.newCachedThreadPool());

        return invoSessionDriverFutureCallback;
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
