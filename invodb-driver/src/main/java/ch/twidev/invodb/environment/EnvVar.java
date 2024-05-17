package ch.twidev.invodb.environment;

import ch.twidev.invodb.drivers.DriverType;

public enum EnvVar {
        SCYLLA_HOSTS(DriverType.SCYLLA),
        SCYLLA_PORT(DriverType.SCYLLA);

        final DriverType type;

        EnvVar(DriverType type) {
                this.type = type;
        }

        @Override
        public String toString() {
                return super.toString().replaceAll("_", "-").toLowerCase();
        }
}