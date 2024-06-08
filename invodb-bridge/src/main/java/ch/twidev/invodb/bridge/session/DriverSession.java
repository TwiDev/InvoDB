package ch.twidev.invodb.bridge.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.operations.InsertContext;
import ch.twidev.invodb.bridge.operations.UpdateContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.util.ResultCallback;

import java.io.Closeable;

public interface DriverSession<Session> extends Closeable {

    void find(FindContext findOperationBuilder, PlaceholderContext placeholderContext, ResultCallback<ElementSet> throwableCallback);
    void findAsync(FindContext findOperationBuilder, PlaceholderContext placeholderContext, ResultCallback<ElementSet> throwableCallback);

    void update(UpdateContext updateContext, PlaceholderContext placeholderContext, ResultCallback<OperationResult> callback);
    void updateAsync(UpdateContext updateContext, PlaceholderContext placeholderContext, ResultCallback<OperationResult> callback);
    void insert(InsertContext updateContext, PlaceholderContext placeholderContext, ResultCallback<ElementSet> callback);
    void insertAsync(InsertContext updateContext, PlaceholderContext placeholderContext, ResultCallback<ElementSet> callback);

    boolean isConnected();

    Session getLegacySession();

}
