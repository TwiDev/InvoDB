package ch.twidev.invodb.bridge.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.util.ThrowableCallback;

import java.io.Closeable;

public interface DriverSession<Session> extends Closeable {

    void find(FindContext findOperationBuilder, ThrowableCallback<ElementSet> throwableCallback);
    void findAsync(FindContext findOperationBuilder, ThrowableCallback<ElementSet> throwableCallback);

    boolean isConnected();

    Session getLegacySession();

}
