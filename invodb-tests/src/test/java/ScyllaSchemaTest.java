import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.mapper.annotations.Field;
import ch.twidev.invodb.mapper.annotations.PrimaryField;
import ch.twidev.invodb.mapper.annotations.Update;
import org.junit.jupiter.api.Test;

public class ScyllaSchemaTest {

    @Test
    public void test() {
        ScyllaUserSchema scyllaUserSchema = new ScyllaUserSchema();

        ScyllaUserSchemaAspect scyllaUserSchemaAspect = scyllaUserSchema.getAspect();


        System.out.println(
                scyllaUserSchema.id
        );

        scyllaUserSchemaAspect.setId(3);

        System.out.println(
                scyllaUserSchema.id
        );
    }
    public static class ScyllaUserSchema extends AspectInvoSchema<ScyllaUserSchemaAspect> implements ScyllaUserSchemaAspect {

        @Field
        @PrimaryField
        private int id = 0;

        public ScyllaUserSchema() {
            super(ScyllaUserSchemaAspect.class, "test");
        }

        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }
    }

    public interface ScyllaUserSchemaAspect {

        @Update(field = "id")
        @Async
        void setId(int id);

    }

}
