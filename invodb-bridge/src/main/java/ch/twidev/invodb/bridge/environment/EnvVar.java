package ch.twidev.invodb.bridge.environment;

import ch.twidev.invodb.bridge.driver.InvoDriverType;

public enum EnvVar {

    URL(null),
    SCYLLA_HOSTS(InvoDriverType.SCYLLA),
    SCYLLA_KEYSPACE(InvoDriverType.SCYLLA),
    SCYLLA_AUTHENTICATOR(InvoDriverType.SCYLLA),
    @Deprecated
    SCYLLA_PORT(InvoDriverType.SCYLLA);

    final InvoDriverType type;

    EnvVar(InvoDriverType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString().replaceAll("_", "-").toLowerCase();
    }

}
