package ch.twidev.invodb.drivers;

import ch.twidev.invodb.authentication.Authenticator;
import ch.twidev.invodb.cluster.ClusterPoint;
import ch.twidev.invodb.cluster.ClusterPoints;
import ch.twidev.invodb.environment.EnvVar;
import ch.twidev.invodb.exceptions.DriverBuilderException;
import ch.twidev.invodb.exceptions.DriverConfigMissingException;
import ch.twidev.invodb.exceptions.DriverConnectionException;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverConfig extends HashMap<EnvVar, Object> {

    private final DriverType driverType;
    private final String name;

    public DriverConfig(DriverType driverType, String name) {
        this.driverType = driverType;
        this.name = name;
    }

    public Object get(EnvVar key) throws DriverConfigMissingException {
        if(!this.containsKey(key))
            throw new DriverConfigMissingException(key, this);

        return super.get(key);
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public String getName() {
        return name;
    }

    public interface DriverBuilder<B> {

        B setDriverName(String name);

        DriverConfig build() throws DriverBuilderException;

    }

    public static class ScyllaBuilder implements DriverBuilder<ScyllaBuilder> {

        private final ClusterPoints contactsPoints = new ClusterPoints();
        private Authenticator authenticator = null;
        private String driverName;
        private String keyspace;

        public ScyllaBuilder addContactPoint(ClusterPoint clusterPoint) {
            this.contactsPoints.add(clusterPoint);

            return this;
        }

        public ScyllaBuilder setKeyspace(String keyspace) {
            this.keyspace = keyspace;

            return this;
        }

        public ScyllaBuilder setAuthenticator(Authenticator authenticator) {
            this.authenticator = authenticator;

            return this;
        }

        @Override
        public ScyllaBuilder setDriverName(String name) {
            this.driverName = name;

            return this;
        }

        @Override
        public DriverConfig build() throws DriverBuilderException {
            return new DriverConfig(DriverType.SCYLLA, driverName){{
                put(EnvVar.SCYLLA_HOSTS, contactsPoints);
                put(EnvVar.SCYLLA_KEYSPACE, keyspace);
                put(EnvVar.SCYLLA_AUTHENTICATOR, authenticator);
            }};
        }

        public ClusterPoints getContactsPoints() {
            return contactsPoints;
        }

        public String getDriverName() {
            return driverName;
        }

        public String getKeyspace() {
            return keyspace;
        }
    }


}
