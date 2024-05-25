package ch.twidev.invodb.bridge.session;

import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.util.ThrowableCallback;

public interface ISession<Session> {

    void find(FindContext findOperationBuilder, ThrowableCallback<Object> throwableCallback);

    boolean isConnected();

    Session getLegacySession();

}
