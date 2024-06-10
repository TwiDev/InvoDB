package ch.twidev.invodb.examples.messages;

import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import ch.twidev.invodb.repository.annotations.FindAll;
import ch.twidev.invodb.repository.annotations.Insert;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import static ch.twidev.invodb.common.query.operations.search.SearchFilter.*;

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

    default Iterator<MessageSchema> findAllByBucket(int bucket, long channelId) {
        return findAllByBucket(
            and(eq("bucket", bucket), eq("channel_id", channelId))
        );
    }

    @FindAll
    Iterator<MessageSchema> findAllByBucket(SearchFilter searchFilter);

}
