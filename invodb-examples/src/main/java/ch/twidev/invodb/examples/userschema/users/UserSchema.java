package ch.twidev.invodb.examples.userschema.users;

import ch.twidev.invodb.common.format.UUIDFormatter;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.UpdateOperationBuilder;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Field;
import ch.twidev.invodb.mapper.annotations.Immutable;
import ch.twidev.invodb.mapper.annotations.PrimaryField;
import ch.twidev.invodb.mapper.annotations.Primitive;
import ch.twidev.invodb.mapper.handler.SchemaOperationHandler;

import java.util.UUID;

public class UserSchema extends AspectInvoSchema<UserSchemaAspect, String>
        implements UserSchemaAspect, SchemaOperationHandler {

    @PrimaryField
    @Field
    @Primitive(formatter = UUIDFormatter.class)
    @Immutable
    private UUID uuid = UUID.randomUUID();

    @Field
    private String name;

    @Field
    private String email;

    @Field
    private int power = 0;

    public UserSchema() {
        super(UserSchemaAspect.class, "uuid");
    }

    @Override
    public String getPrimaryValue() {
        return uuid.toString();
    }

    @Override
    public void setEmailAsync(String email) {
        this.email = email;
    }

    @Override
    public void setPowerAsync(int power) {
        this.power = power;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getPower() {
        return power;
    }

    @Override
    public String toString() {
        return "UserSchema{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", power=" + power +
                '}';
    }

    @Override
    public void onSuccess(InvoQuery<?> invoQuery) {
        if(invoQuery instanceof UpdateOperationBuilder updateOperationBuilder) {
            System.out.println("Update Interface with new value for "  + updateOperationBuilder.getFields().getKeysString());
        }

    }
}
