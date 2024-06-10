package ch.twidev.invodb.examples.messages;

import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import ch.twidev.invodb.repository.annotations.Insert;

import java.util.concurrent.CompletableFuture;

public interface MessageRepository extends SchemaRepositoryProvider<MessageSchema> {

    @Insert(fields = {"messageId", "bucket", "channelId", "authorId", "content"})
    @Async
    CompletableFuture<MessageSchema> insert(
            long messageId,
            int bucket,
            long channelId,
            long authorId,
            String content
    );

}
