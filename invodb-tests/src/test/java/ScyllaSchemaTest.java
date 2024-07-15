import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.common.format.UUIDFormatter;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.*;
import ch.twidev.invodb.mapper.handler.SchemaOperationHandler;
import ch.twidev.invodb.repository.SchemaRepository;
import ch.twidev.invodb.repository.annotations.Find;
import ch.twidev.invodb.repository.annotations.FindOrInsert;
import ch.twidev.invodb.repository.annotations.Insert;
import ch.twidev.invodb.repository.annotations.Update;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.UUID;
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

/*        try {
            ScyllaConnection scyllaCluster = new ScyllaCluster(driverConfig).connectSession("main");

            ScyllaUserRepository scyllaUserRepository = new SchemaRepositoryProvider<>(scyllaCluster, "users", ScyllaUserRepository.class){}.build();

            // Find user
            {
                ScyllaUserSchema schema = scyllaUserRepository.findByName("TwiDev");

                logger.info("[Find] " + schema.toString()); //Output: ScyllaUserSchema{id=1, email='twidev5@gmail.com', name='TwiDev'}

                Monitoring monitoring = new Monitoring("Change Email");
                schema.getAspect().setEmailAsync("helloworld@gmail.com");
                logger.info("[Find] " + schema.getEmail());
                monitoring.done();
            }

            // Insert user
            {
                Monitoring monitoring = new Monitoring("Insert User");
                ScyllaUserSchema schema = scyllaUserRepository.insertUser(11, "dkmsai@gmail.com", "Hello");
                monitoring.done();

                logger.info("[Insert] " + schema.toString());
            }

            // Async find user by primary key
            {
                Monitoring monitoring = new Monitoring("Find Async");
                scyllaUserRepository.findByIdAsync(11).thenAccept(scyllaUserSchema -> {
                    monitoring.done();

                    logger.info("[AsyncFind] Thread " + Thread.currentThread().getName());
                    logger.info("[AsyncFind] " + scyllaUserSchema.toString());
                }).exceptionally(throwable -> {
                    throwable.printStackTrace();

                    return null;
                });
            }

            logger.info("[Main] Thread " + Thread.currentThread().getName());

            try {
                // Waiting for async task to finish
                Thread.sleep(4000);

                scyllaCluster.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } catch (DriverConnectionException e) {
            throw new RuntimeException(e);
        }*/

    }
    public static class ScyllaUserSchema extends AspectInvoSchema<ScyllaUserSchemaAspect, Integer> implements ScyllaUserSchemaAspect, SchemaOperationHandler {

        @Field
        @PrimaryField
        @Immutable
        private int id = 0;

        @Field
        private String email;

        @Field
        private String name;

        @Field
        @Primitive(formatter = UUIDFormatter.class)
        private UUID uuid = UUID.randomUUID();

        @Override
        public void setEmailAsync(String email) {
            this.email = email;
        }

        @Override
        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public UUID getUuid() {
            return uuid;
        }

        @Override
        public void onSuccess(InvoQuery<?> invoQuery) {
            logger.info("query success " + invoQuery.getQueryOperation());
        }

        @Override
        public void onFailed(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public String toString() {
            return "ScyllaUserSchema{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", name='" + name + '\'' +
                    ", uuid=" + uuid +
                    '}';
        }
    }

    public interface ScyllaUserSchemaAspect {

        @Update(field = "email")
        @Async
        void setEmailAsync(String email);

        @Update(field = "uuid")
        @Async
        void setUuid(UUID uuid);

    }

    public interface ScyllaUserRepository extends SchemaRepository<ScyllaUserSchema> {

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
