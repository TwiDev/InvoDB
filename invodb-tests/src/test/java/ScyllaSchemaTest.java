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
                scyllaUserSchema.getId()
        );

        scyllaUserSchemaAspect.setId(3);

        System.out.println(
                scyllaUserSchema.getId()
        );
    }
    public static class ScyllaUserSchema extends AspectInvoSchema<ScyllaUserSchemaAspect, Integer> implements ScyllaUserSchemaAspect {

        @Field
        @PrimaryField
        private int id = 0;

        public ScyllaUserSchema() {
            super(ScyllaUserSchemaAspect.class, "id", "test");
        }

        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }

        @Override
        public Integer getPrimaryValue() {
            return id;
        }
    }

    public interface ScyllaUserSchemaAspect {

        @Update(field = "id")
        @Async
        void setId(int id);

    }

}
