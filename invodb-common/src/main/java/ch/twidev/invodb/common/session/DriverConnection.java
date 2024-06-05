package ch.twidev.invodb.common.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.OperationContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ResultCallback;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;

@SuppressWarnings("unchecked")
public class DriverConnection {

    public static <Session, R> void runQuery(DriverSession<Session> session,
                                             Class<R> resultInstance,
                                             InvoQuery<R> invoQuery,
                                             ResultCallback<R> throwableCallback,
                                             PlaceholderContext placeholderContext) {

        if(placeholderContext == null && invoQuery instanceof OperationContext operationContext) {
            placeholderContext = operationContext.getPlaceHolder();
        }

        switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> {
                session.find(findOperationBuilder, placeholderContext, (ResultCallback<ElementSet>) throwableCallback);
            }
            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        }

    }

    public static <Session, R> void runQueryAsync(DriverSession<Session> session,
                                                  Class<R> resultInstance,
                                                  InvoQuery<R> invoQuery,
                                                  ResultCallback<R> throwableCallback,
                                                  PlaceholderContext placeholderContext) {


        if(placeholderContext == null && invoQuery instanceof OperationContext operationContext) {
            placeholderContext = operationContext.getPlaceHolder();
        }

        switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> {
                session.findAsync(findOperationBuilder, placeholderContext, (ResultCallback<ElementSet>) throwableCallback);
            }
            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        }
    }

}
