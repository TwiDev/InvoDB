package ch.twidev.invodb.common.drivers;

import java.util.concurrent.CompletableFuture;

public interface ClusterDriver<S> {

    S connect(String space);
    CompletableFuture<S> asyncConnect(String space);

}
