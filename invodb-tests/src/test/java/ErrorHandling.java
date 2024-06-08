import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.*;
import ch.twidev.invodb.mapper.handler.SchemaOperationHandler;
import ch.twidev.invodb.repository.SchemaRepository;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import ch.twidev.invodb.repository.annotations.Find;
import ch.twidev.invodb.repository.annotations.FindOrInsert;
import ch.twidev.invodb.repository.annotations.Insert;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ErrorHandling {

    private static final Logger logger = Logger.getLogger("ScyllaExample");

    @Test
    public void find() {
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
                Optional.ofNullable(scyllaUserRepository.findByName("TwiDev")).ifPresentOrElse(scyllaUserSchema -> {
                    logger.info(scyllaUserSchema.toString());

                    scyllaUserSchema.getAspect().setEmailAsync(null);

                    logger.info(scyllaUserSchema.toString());
                    System.out.println(scyllaUserSchema.getEmail());
                },() -> logger.warning("Schema is null"));
            }
        } catch (DriverConnectionException driverConnectionException){
            throw new RuntimeException(driverConnectionException);
        }
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

        public ScyllaUserSchema() {
            super(ScyllaUserSchemaAspect.class, "id");
        }

        @Override
        public void setEmailAsync(String email) {
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
        public void onSuccess(InvoQuery<?> invoQuery) {
            System.out.println("query success " + invoQuery.getQueryOperation());
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
                    '}';
        }
    }

    public interface ScyllaUserSchemaAspect {

        @Update(field = "email")
        @Async
        void setEmailAsync(String name);

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
