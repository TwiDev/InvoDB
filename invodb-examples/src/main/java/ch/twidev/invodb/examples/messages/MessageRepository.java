package ch.twidev.invodb.examples.messages;

import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.repository.MutableSchemaRepository;
import ch.twidev.invodb.repository.annotations.Aspect;
import ch.twidev.invodb.repository.annotations.Get;
import ch.twidev.invodb.repository.SchemaRepository;
import ch.twidev.invodb.repository.annotations.FindAll;
import ch.twidev.invodb.repository.annotations.Insert;

import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static ch.twidev.invodb.common.query.operations.search.SearchFilter.*;

public interface MessageRepository extends MutableSchemaRepository<MessageSchema, MessageAspect> {

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

    default Iterator<MessageSchema> findAllByBuckets(long channelId, IntStream bucket) {
        SearchFilter[] searchFilter = bucket.mapToObj(o -> eq("bucket",o)).toArray(SearchFilter[]::new);


        return findAllByBucket(
                and(or(searchFilter), eq("channel_id", channelId))
        );
    }


    @Aspect
    MessageAspect getAspect(long channelId, long messageId, int bucketId);

    @FindAll
    Iterator<MessageSchema> findAllByBucket(SearchFilter searchFilter);

    @Get(field = "content")
    @Async
    CompletableFuture<String> getContent(long messageId);

}
