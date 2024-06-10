package ch.twidev.invodb.common.util;

import java.util.logging.Logger;

public class Monitoring {

    private static final Logger logger = Logger.getLogger("TestMonitoring");

    private final long time;

    private final String name;

    public Monitoring() {
        this(null);
    }

    public Monitoring(String name) {
        this.time = System.nanoTime();

        this.name = name;
    }

    public void done() {
        long executionTime = (System.nanoTime() - time) / 1_000_000L;

        logger.warning("[Monitoring] Execution Time" + ((name != null) ? " of " + name : " ") + " took " + executionTime + "ms");

    }
}
