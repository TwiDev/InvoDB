package ch.twidev.invodb.examples.messages;

import ch.twidev.invodb.cache.SchemaCache;
import ch.twidev.invodb.cache.SchemaCacheProvider;
import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Field;
import ch.twidev.invodb.mapper.annotations.Immutable;
import ch.twidev.invodb.mapper.annotations.PrimaryField;
import org.redisson.api.RedissonClient;

import java.util.logging.Logger;

public class MessageSchema extends AspectInvoSchema<MessageAspect, Long> implements MessageAspect {

    @PrimaryField
    @Field(name = "message_id")
    @Immutable
    private long messageId;

    @Field(name = "channel_id")
    @Immutable
    private long channelId;

    @Field
    @Immutable
    private int bucket;

    @Field(name = "author_id")
    @Immutable
    private long authorId;

    @Field(name = "content")
    private String content = null;


    public MessageSchema() {
        super(MessageAspect.class, "message_id");
    }

    @Override
    public void onPopulated() {

    }

    @Override
    public Long getPrimaryValue() {
        return messageId;
    }

    @Override
    public String toString() {
        return "MessageSchema{" +
                "messageId=" + messageId +
                ", channelId=" + channelId +
                ", bucket=" + bucket +
                ", authorId=" + authorId +
                ", content='" + content + '\'' +
                '}';
    }
}
