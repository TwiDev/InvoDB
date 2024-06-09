package ch.twidev.invodb.examples.userschema.users;

import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.repository.SchemaRepository;

public class UserFactory<Session> {

    private static UserSchemaProvider userSchemaProvider;

    public UserFactory(DriverSession<Session> connection) {
        userSchemaProvider = new SchemaRepository<>(
                connection,
                "users_schema",
                UserSchemaProvider.class){}.build();
    }

    public static UserSchemaProvider getProvider() {
        return userSchemaProvider;
    }
}
