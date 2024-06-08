package ch.twidev.invodb.common.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.OperationContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ResultCallback;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.builder.InsertOperationBuilder;
import ch.twidev.invodb.common.query.builder.UpdateOperationBuilder;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class DriverConnection {

    public static <Session, R> R runQuery(DriverSession<Session> session,
                                                                  Class<R> resultInstance,
                                                                  InvoQuery<R> invoQuery,
                                                                  PlaceholderContext placeholderContext) {

        if(placeholderContext == null && invoQuery instanceof OperationContext operationContext) {
            placeholderContext = operationContext.getPlaceHolder();
        }

        switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> {
                return (R) session.find(findOperationBuilder, placeholderContext);
            }
            case UpdateOperationBuilder updateOperationBuilder -> {
                return (R) session.update(updateOperationBuilder, placeholderContext);
            }
            case InsertOperationBuilder insertOperationBuilder -> {
                return (R) session.insert(insertOperationBuilder, placeholderContext);
            }
            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        }

    }

    public static <Session, R> CompletableFuture<R> runQueryAsync(DriverSession<Session> session,
                                                  Class<R> resultInstance,
                                                  InvoQuery<R> invoQuery,
                                                  PlaceholderContext placeholderContext) {


        if(placeholderContext == null && invoQuery instanceof OperationContext operationContext) {
            placeholderContext = operationContext.getPlaceHolder();
        }

        switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> {
                return (CompletableFuture<R>) session.findAsync(findOperationBuilder, placeholderContext);
            }
            case UpdateOperationBuilder updateOperationBuilder -> {
                return (CompletableFuture<R>) session.updateAsync(updateOperationBuilder, placeholderContext);
            }
            case InsertOperationBuilder insertOperationBuilder -> {
                return (CompletableFuture<R>) session.insertAsync(insertOperationBuilder, placeholderContext);
            }
            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        }
    }

}
