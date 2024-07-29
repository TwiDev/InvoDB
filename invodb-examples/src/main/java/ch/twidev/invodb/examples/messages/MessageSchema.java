package ch.twidev.invodb.examples.messages;

import ch.twidev.invodb.mapper.AspectInvoSchema;
import ch.twidev.invodb.mapper.annotations.Field;
import ch.twidev.invodb.mapper.annotations.Immutable;
import ch.twidev.invodb.mapper.annotations.PrimaryField;

public class MessageSchema extends AspectInvoSchema<MessageAspect> implements MessageAspect {

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

    @Override
    public void setContent(String msg) {
        this.content = msg;
    }
}
