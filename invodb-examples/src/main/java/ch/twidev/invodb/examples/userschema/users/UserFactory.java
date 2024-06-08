package ch.twidev.invodb.examples.userschema.users;

import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.repository.SchemaRepository;

public class UserFactory {

    private static UserSchemaProvider userSchemaProvider;

    public static void init(ScyllaConnection scyllaConnection) {
        userSchemaProvider = new SchemaRepository<>(
                scyllaConnection,
                "users_schema",
                UserSchemaProvider.class){}.build();
    }

    public static UserSchemaProvider getProvider() {
        return userSchemaProvider;
    }
}
