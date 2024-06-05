package ch.twidev.invodb.bridge.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.util.ResultCallback;

import java.io.Closeable;

public interface DriverSession<Session> extends Closeable {

    void find(FindContext findOperationBuilder, PlaceholderContext placeholderContext, ResultCallback<ElementSet> throwableCallback);
    void findAsync(FindContext findOperationBuilder, PlaceholderContext placeholderContext, ResultCallback<ElementSet> throwableCallback);

    boolean isConnected();

    Session getLegacySession();

}
