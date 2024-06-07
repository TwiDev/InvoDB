import ch.twidev.invodb.bridge.placeholder.QueryPlaceholder;
import ch.twidev.invodb.common.format.DataFormat;
import ch.twidev.invodb.common.format.JsonFormatter;
import ch.twidev.invodb.common.format.UUIDFormatter;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

public class ScyllaSchemaTest {

    @Test
    public void test() {

        Object key = DataFormat.getPrimitive(new HashMap<String, Object>(){{
            put("test3", 1);
            put("taddsa1", 2);
            put("test1", "334324dasd");
            put("sd", "d");
        }}, JsonFormatter.class);

        Object key2 = DataFormat.getPrimitive(UUID.randomUUID());

        System.out.println(key);
        System.out.println(key2);

        HashMap<String, Object> hashmap = DataFormat.getFromPrimitive(key, HashMap.class, JsonFormatter.class);
        System.out.println(hashmap.toString());

    }
    public static class ScyllaUserSchema extends AspectInvoSchema<ScyllaUserSchemaAspect, Integer> implements ScyllaUserSchemaAspect {

        @Field
        @PrimaryField
        private int id = 0;

        @Field
        @Placeholder
        @Primitive(formatter = UUIDFormatter.class)
        private UUID uuid;

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
