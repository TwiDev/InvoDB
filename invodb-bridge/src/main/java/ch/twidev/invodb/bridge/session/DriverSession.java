package ch.twidev.invodb.bridge.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.operations.InsertContext;
import ch.twidev.invodb.bridge.operations.UpdateContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.util.ResultCallback;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

public interface DriverSession<Session> extends Closeable {

    ElementSet find(FindContext findOperationBuilder, PlaceholderContext placeholderContext);
    CompletableFuture<ElementSet> findAsync(FindContext findOperationBuilder, PlaceholderContext placeholderContext);

    OperationResult update(UpdateContext updateContext, PlaceholderContext placeholderContext);
    CompletableFuture<OperationResult> updateAsync(UpdateContext updateContext, PlaceholderContext placeholderContext);
    OperationResult insert(InsertContext updateContext, PlaceholderContext placeholderContext);
    CompletableFuture<OperationResult> insertAsync(InsertContext updateContext, PlaceholderContext placeholderContext);

    boolean isConnected();

    Session getLegacySession();

}
