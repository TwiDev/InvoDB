package ch.twidev.invodb.examples.userschema.users;

import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;

public class UserFactory<Session> {

    private static UserSchemaProvider userSchemaProvider;

    public UserFactory(DriverSession<Session> connection) {
        userSchemaProvider = new SchemaRepositoryProvider<>(
                connection,
                "users_schema",
                UserSchemaProvider.class){}.build();
    }

    public static UserSchemaProvider getProvider() {
        return userSchemaProvider;
    }
}
