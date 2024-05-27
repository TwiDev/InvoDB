package ch.twidev.invodb.bridge.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.util.ThrowableCallback;

public interface DriverSession<Session> {

    void find(FindContext findOperationBuilder, ThrowableCallback<ElementSet> throwableCallback);

    boolean isConnected();

    Session getLegacySession();

}
