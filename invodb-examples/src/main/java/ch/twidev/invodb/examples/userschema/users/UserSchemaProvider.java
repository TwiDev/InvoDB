package ch.twidev.invodb.examples.userschema.users;

import ch.twidev.invodb.common.format.UUIDFormatter;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.mapper.annotations.Primitive;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import ch.twidev.invodb.repository.annotations.Find;
import ch.twidev.invodb.repository.annotations.Insert;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserSchemaProvider extends SchemaRepositoryProvider<UserSchema> {

    @Find(by = "uuid")
    @Async
    CompletableFuture<UserSchema> findAsync(UUID uuid);

    @Find(by = "uuid")
    @Primitive(formatter = UUIDFormatter.class)
    UserSchema find(UUID uuid);

    @Find(by = "email")
    UserSchema findByEmail(String email);

    @Insert(fields = {"name","email"})
    UserSchema insert(String name, String email);
}
