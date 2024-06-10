package ch.twidev.invodb.examples.messages.channel;

import ch.twidev.invodb.examples.messages.MessageApplication;

public class Channel {

    private final long channelId;

    public Channel() {
        this.channelId = MessageApplication.getInstance().getSnowflake().nextId();
    }

    public Channel(long channelId) {
        this.channelId = channelId;
    }

    public long getChannelId() {
        return channelId;
    }
}
