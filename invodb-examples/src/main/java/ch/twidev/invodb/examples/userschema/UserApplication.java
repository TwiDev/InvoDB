package ch.twidev.invodb.examples.userschema;

import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.config.URLDriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.driver.mongodb.MongoCluster;
import ch.twidev.invodb.driver.mongodb.MongoConnection;
import ch.twidev.invodb.driver.mongodb.URLMongoConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.examples.userschema.users.UserFactory;
import ch.twidev.invodb.examples.userschema.users.UserSchema;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class UserApplication {

    private static final Logger logger = Logger.getLogger("ScyllaExample");

    private final DriverSession<?> connection;

    public UserApplication() throws DriverConnectionException {
        DriverConfig scyllaConfig = new ScyllaConfigBuilder()
                .setDriverName("ScyllaDriver")
                .addContactPoint(new ContactPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                .setAuthProvider(new PlainTextAuth("cassandra", "cassandra"))
                .build();

        URLDriverConfig mongoConfig =
                new URLMongoConfigBuilder("mongodb://localhost:27017/").build();

        MongoConnection connection = new MongoCluster(mongoConfig)
                .connectSession("main");

        /*ScyllaConnection connection = new ScyllaCluster(scyllaConfig)
                .connectSession("main");*/

        this.connection = connection;

        new UserFactory(connection);
    }

    public static void main(String[] args) throws DriverConnectionException {
        UserApplication userApplication = new UserApplication();

        // Find a user
        {
            UserSchema userSchema = UserFactory.getProvider().find(
                    UUID.fromString("e28139ac-a2e8-4b4b-a4bc-b1ed472cac76"));

            logger.info(userSchema.toString());

            // Update user email
            userSchema.getAspect().setEmailAsync("twidev15@gmail.com");
        }

        // Login User
        {
            userApplication.login("twidev10@gmail.com").ifPresentOrElse(userSchema -> {
                logger.info("Your user account : " + userSchema.toString());

                userSchema.getAspect().setPowerAsync(10);

                logger.info("Your new user account : " + userSchema.toString());
            }, () -> {
                logger.severe("No account found !");
            });
        }

        try {
            // Waiting for async task to finish
            Thread.sleep(4000);

            userApplication.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserSchema register(String name, String email) {
        return UserFactory.getProvider().insert(name, email);
    }

    public Optional<UserSchema> login(String email) {
        return Optional.ofNullable(UserFactory.getProvider().findByEmail(email));
    }

    public DriverSession<?> getConnection() {
        return connection;
    }
}
