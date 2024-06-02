package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.auth.AuthenticatorProvider;
import ch.twidev.invodb.bridge.driver.InvoClusterDriver;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.cluster.ClusterPoints;
import ch.twidev.invodb.bridge.driver.config.URLDriverConfig;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigMissingException;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;

import ch.twidev.invodb.bridge.util.ThrowableCallback;
import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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



        CountDownLatch latch = new CountDownLatch(1);
        Futures.addCallback(asyncTask, new FutureCallback<>() {
            @Override
            public void onSuccess(Session session) {
                synchronized (this) {
                    scyllaConnectionThrowableCallback.run(new ScyllaConnection(session), null);
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(@NotNull Throwable throwable) {
                synchronized (this) {
                    scyllaConnectionThrowableCallback.run(null, throwable);
                    latch.countDown();
                }
            }
        }, executorService);

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
        });

        return invoSessionDriverFutureCallback;
    }

    @Override
    public void close() {
        cluster.close();
    }

}
