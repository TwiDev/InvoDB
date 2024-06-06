import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.*;
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

        @Field
        @Placeholder
        private String name;

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

    public enum UserPlaceHolder implements QueryPlaceholder {

        USER_ID("id");

        private final String name;

        UserPlaceHolder(String name) {
            this.name = name;
        }

        @Override
        public String getPlaceholder() {
            return name;
        }
    }


}
