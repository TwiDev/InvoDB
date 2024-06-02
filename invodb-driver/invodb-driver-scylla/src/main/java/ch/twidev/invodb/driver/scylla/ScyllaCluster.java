package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.driver.InvoClusterDriver;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ClusterPoints;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.config.URLDriverConfig;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigMissingException;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.scheduler.Scheduler;
import ch.twidev.invodb.bridge.util.ThrowableCallback;
import ch.twidev.invodb.common.query.InvoQuery;
import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public class ScyllaCluster extends InvoClusterDriver<Session, ScyllaConnection> {

    private Cluster cluster;

    private static final String CLUSTER_DRIVER_KEY = "ScyllaClusterDriver";

    public ScyllaCluster(DriverConfig driverConfig) throws DriverConnectionException {
        super(driverConfig, InvoDriverType.SCYLLA);

        this.initDriver();
    }

    @Override
    public void initDriver() throws DriverConnectionException {
        DriverConfig driverConfig = this.getDriverConfig();

        if(driverConfig instanceof URLDriverConfig) {
            return;
        }

        if(!(driverConfig.get(EnvVar.SCYLLA_HOSTS) instanceof ClusterPoints clusterPoints))
            throw new DriverConfigMissingException(EnvVar.SCYLLA_HOSTS, driverConfig);

        if(!(driverConfig.get(EnvVar.SCYLLA_AUTHENTICATOR) instanceof PlainTextAuth authenticator))
            throw new DriverConfigMissingException(EnvVar.SCYLLA_AUTHENTICATOR, driverConfig);


        Cluster.Builder clusterBuilder = Cluster.builder()
                .withClusterName(CLUSTER_DRIVER_KEY)
                .addContactPointsWithPorts(
                        clusterPoints.stream()
                                .map(ContactPoint::inetSocketAddress)
                                .toList());

        if(authenticator.isEmpty()) {
            clusterBuilder.withAuthProvider(AuthProvider.NONE);
        }else{
            clusterBuilder.withAuthProvider(new PlainTextAuthProvider(authenticator.username(), authenticator.password()));
        }

        this.cluster = clusterBuilder.build();
    }

    @Override
    public boolean exists() {
        return cluster != null && !cluster.isClosed();
    }

    @Override
    public ScyllaConnection connectSession(String keyname) {
        return new ScyllaConnection(this.cluster.connect(keyname));
    }


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void asyncConnectSession(String keyname, ThrowableCallback<ScyllaConnection> scyllaConnectionThrowableCallback) {
        ListenableFuture<Session> asyncTask = this.cluster.connectAsync(keyname);

        Futures.addCallback(asyncTask, new FutureCallback<>() {
            @Override
            public void onSuccess(Session session) {
                scyllaConnectionThrowableCallback.run(new ScyllaConnection(session), null);
            }

            @Override
            public void onFailure(@NotNull Throwable throwable) {
                scyllaConnectionThrowableCallback.run(null, throwable);
            }
        }, executorService);
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
        }, executorService);

        return invoSessionDriverFutureCallback;
    }

    @Override
    public void close() {
        //cluster.close();
    }

}
