import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Field;
import ch.twidev.invodb.mapper.annotations.PrimaryField;
import ch.twidev.invodb.mapper.annotations.Update;
import ch.twidev.invodb.repository.SchemaRepository;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import ch.twidev.invodb.repository.annotations.Find;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public class ScyllaSchemaTest {

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
            scyllaUserRepository.findByName("TwiDev").thenAccept(scyllaUserSchema -> {

                System.out.println(scyllaUserSchema.toString()); //Output : ScyllaUserSchema{id=1, email='twidev5@gmail.com', name='TwiDev'}

                scyllaUserSchema.getAspect().setEmail("twidev394@gmail.com");

                System.out.println(scyllaUserSchema.getEmail());

            }).exceptionally(throwable -> {
                throwable.printStackTrace();

                return null;
            });

            scyllaCluster.close();
        } catch (DriverConnectionException e) {
            throw new RuntimeException(e);
        }

    }
    public static class ScyllaUserSchema extends AspectInvoSchema<ScyllaUserSchemaAspect, Integer> implements ScyllaUserSchemaAspect {

        @Field
        @PrimaryField
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

        @Find(by = "name")
        CompletableFuture<ScyllaUserSchema> findByName(String name);

    }


}
