import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.*;
import ch.twidev.invodb.repository.SchemaRepository;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import ch.twidev.invodb.repository.annotations.Find;
import ch.twidev.invodb.repository.annotations.FindOrInsert;
import ch.twidev.invodb.repository.annotations.Insert;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ScyllaSchemaTest {

    private static final Logger logger = Logger.getLogger("ScyllaExample");

    @Test
    public void test() {
        DriverConfig driverConfig = new ScyllaConfigBuilder()
                .setDriverName("ScyllaDriver")
                .addContactPoint(new ContactPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                .setAuthProvider(new PlainTextAuth("cassandra", "cassandra"))
                .build();

        try {
            ScyllaConnection scyllaCluster = new ScyllaCluster(driverConfig).connectSession("main");

            ScyllaUserRepository scyllaUserRepository = new SchemaRepository<>(scyllaCluster, "users", ScyllaUserRepository.class){}.build();

            // Find user
            {
                ScyllaUserSchema schema = scyllaUserRepository.findByName("TwiDev");

                logger.info(schema.toString()); //Output : ScyllaUserSchema{id=1, email='twidev5@gmail.com', name='TwiDev'}
                schema.getAspect().setEmail("twidev398@gmail.com");
                logger.info(schema.getEmail());
            }

            // Insert user
            {
                ScyllaUserSchema schema = scyllaUserRepository.insertUser(467,"test683@gmail.com", "Hello3343");

                logger.info(schema.toString());
                schema.getAspect().setEmail("world@gmail.com");
                logger.info(schema.toString());
            }

            // Async find user by primary key
            {
                scyllaUserRepository.findByIdAsync(10).thenAccept(scyllaUserSchema -> {
                    logger.info(Thread.currentThread().getName());
                    logger.info(scyllaUserSchema.toString());
                }).exceptionally(throwable -> {
                    throwable.printStackTrace();

                    return null;
                });
            }

            logger.info(Thread.currentThread().getName());

            try {
                // Waiting for async task to finish
                Thread.sleep(4000);

                scyllaCluster.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } catch (DriverConnectionException e) {
            throw new RuntimeException(e);
        }

    }
    public static class ScyllaUserSchema extends AspectInvoSchema<ScyllaUserSchemaAspect, Integer> implements ScyllaUserSchemaAspect {

        @Field
        @PrimaryField
        @Immutable
        private int id = 0;

        @Field
        private String email;

        @Field
        private String name;

        public ScyllaUserSchema() {
            super(ScyllaUserSchemaAspect.class, "id");
        }

        @Override
        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public Integer getPrimaryValue() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return "ScyllaUserSchema{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public interface ScyllaUserSchemaAspect {

        @Update(field = "email")
        void setEmail(String email);

    }

    public interface ScyllaUserRepository extends SchemaRepositoryProvider<ScyllaUserSchema> {

        @Find(by = "id")
        @Async
        CompletableFuture<ScyllaUserSchema> findByIdAsync(int id);

        @Find(by = "name")
        ScyllaUserSchema findByName(String name);

        @Insert(fields = {"id","email","name"})
        ScyllaUserSchema insertUser(int id, String email, String name);

        @FindOrInsert(find = @Find(by = "name"), insert = @Insert(fields = {"id","name","email"}))
        ScyllaUserSchema getOrInsertUser(int id, String name, String email);

    }


}
