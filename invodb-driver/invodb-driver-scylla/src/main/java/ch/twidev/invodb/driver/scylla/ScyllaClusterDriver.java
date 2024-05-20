package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.common.authentication.AuthenticatorProvider;
import ch.twidev.invodb.common.authentication.NoneAuthenticator;
import ch.twidev.invodb.common.cluster.ClusterPoint;
import ch.twidev.invodb.common.cluster.ClusterPoints;
import ch.twidev.invodb.common.drivers.*;
import ch.twidev.invodb.common.drivers.statements.PreparedStatementConnection;
import ch.twidev.invodb.common.environment.EnvVar;
import ch.twidev.invodb.common.exceptions.DriverConfigMissingException;
import ch.twidev.invodb.common.exceptions.DriverConnectionException;
import ch.twidev.invodb.common.exceptions.PrepareStatementException;
import ch.twidev.invodb.common.utils.Callback;
import ch.twidev.invodb.common.utils.NonNull;
import com.datastax.driver.core.*;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@SuppressWarnings({"UnstableApiUsage"})
public class ScyllaClusterDriver extends InvoDriver<Cluster> implements ClusterDriver<ScyllaClusterDriver.ScyllaSession> {

    private static final String CLUSTER_DRIVER_KEY = "ScyllaClusterDriver";


    public ScyllaClusterDriver(DriverConfig driverConfig) {
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


    @Override
    public ScyllaSession connect(String space) {
        return null;
    }

    @Override
    public CompletableFuture<ScyllaSession> asyncConnect(String keyspace) {
        CompletableFuture<ScyllaSession> invoSessionDriverFutureCallback = new CompletableFuture<>();
        ScyllaClusterDriver scyllaClusterDriver = this;

        ListenableFuture<Session> asyncTask = this.getConnection().connectAsync(keyspace);

        Futures.addCallback(asyncTask, new com.google.common.util.concurrent.FutureCallback<>() {
            @Override
            public void onSuccess(@Nullable Session session) {
                System.out.println("hell");

                invoSessionDriverFutureCallback.complete(
                        new ScyllaSession(scyllaClusterDriver, session)
                );
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {
                invoSessionDriverFutureCallback.completeExceptionally(throwable);
            }
        }, Executors.newCachedThreadPool());

        return invoSessionDriverFutureCallback;
    };

    public static class ScyllaSession implements DriverConnection<ScyllaClusterDriver>, PreparedStatementConnection<PreparedStatement> {

        private final ScyllaClusterDriver clusterDriver;
        private final Session session;

        public ScyllaSession(ScyllaClusterDriver scyllaClusterDriver, Session session) {
            this.clusterDriver = scyllaClusterDriver;
            this.session = session;
        }

        public Session getSession() {
            return session;
        }

        @Override
        public ScyllaClusterDriver getDriver() {
            return clusterDriver;
        }

        @Override
        public boolean isConnected() {
            return !session.isClosed();
        }

        @Override
        public void asyncQuery(String query, Callback<?> callback, Object... vars) {

        }

        @Override
        public void query(String query, Callback<?> callback, Object... vars) {

        }

        @Override
        public void execute(String query, Object... vars) {
            PreparedStatement preparedStatement = this.prepareStatement(query, vars);
            ResultSet rows = this.session.execute(preparedStatement.bind());
            System.out.println(rows.all());
        }

        @Override
        public void asyncExecute(String query, Object... vars) {

        }

        @Override
        public PreparedStatement prepareStatement(String query, Object... vars) throws PrepareStatementException {
            try {
                return session.prepare(new SimpleStatement(query, vars));
            } catch (Exception cause) {
                throw new PrepareStatementException(cause);
            }
        }
    }

    @Override
    public void closeConnection() {
        this.getConnection().close();
    }

    @Override
    public boolean isConnected() {
        return this.getConnection() != null && !this.getConnection().isClosed();
    }
}
