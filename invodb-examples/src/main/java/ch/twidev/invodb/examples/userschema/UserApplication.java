package ch.twidev.invodb.examples.userschema;

import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.examples.userschema.users.UserFactory;
import ch.twidev.invodb.examples.userschema.users.UserSchema;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.logging.Logger;

public class UserApplication {

    private static final Logger logger = Logger.getLogger("ScyllaExample");

    private final ScyllaConnection scyllaConnection;

    public UserApplication() throws DriverConnectionException {
        DriverConfig driverConfig = new ScyllaConfigBuilder()
                .setDriverName("ScyllaDriver")
                .addContactPoint(new ContactPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                .setAuthProvider(new PlainTextAuth("cassandra", "cassandra"))
                .build();

        this.scyllaConnection = new ScyllaCluster(driverConfig)
                .connectSession("main");

        UserFactory.init(scyllaConnection);
    }

    public static void main(String[] args) throws DriverConnectionException {
        UserApplication userApplication = new UserApplication();

        // Register a user
        {
            UserSchema userSchema = userApplication.register("TwiDev", "twidev5@gmail.com");
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
        scyllaConnection.close();
    }

    public UserSchema register(String name, String email) {
        return UserFactory.getProvider().insert(name, email);
    }

    public Optional<UserSchema> login(String email) {
        return Optional.ofNullable(UserFactory.getProvider().findByEmail(email));
    }

    public ScyllaConnection getScyllaConnection() {
        return scyllaConnection;
    }
}
