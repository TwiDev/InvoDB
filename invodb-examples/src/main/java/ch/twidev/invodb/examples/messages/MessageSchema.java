package ch.twidev.invodb.examples.messages;

import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Field;
import ch.twidev.invodb.mapper.annotations.Immutable;
import ch.twidev.invodb.mapper.annotations.PrimaryField;

import java.util.logging.Logger;

public class MessageSchema extends AspectInvoSchema<MessageAspect, Long> implements MessageAspect {

    @PrimaryField
    @Field(name = "message_id")
    @Immutable
    private long messageId;

    @PrimaryField
    @Field(name = "channel_id")
    @Immutable
    private long channelId;

    @PrimaryField
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
    public Long getPrimaryValue() {
        return messageId;
    }
}