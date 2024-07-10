package ch.twidev.invodb.examples.messages;

import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.util.Monitoring;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import ch.twidev.invodb.examples.messages.channel.Channel;
import ch.twidev.invodb.examples.messages.snowflake.Snowflake;
import ch.twidev.invodb.repository.SchemaRepositoryProvider;
import com.datastax.driver.core.Session;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class MessageApplication<S> {

    private static final Logger logger = Logger.getLogger("Messages");

    public static final long EPOCH = 1420070400000L;

    public static final long BUCKET_SIZE = 60 * 1000L; // 60s

    private static MessageApplication<?> INSTANCE;

    private final DriverSession<S> driverSession;

    private final MessageRepository messageRepository;

    private final long nodeId;

    private final Snowflake snowflake;

    public MessageApplication(DriverSession<S> driverSession) {
        INSTANCE = this;

        this.driverSession = driverSession;

        this.snowflake = new Snowflake();
        this.nodeId = snowflake.getNodeId();

        logger.info("Loading message application with node id " + nodeId);

        this.messageRepository = new SchemaRepositoryProvider<>(
                driverSession,
                "messages",
                MessageRepository.class)
        {}.build();
    }

    public static void main(String[] args) throws DriverConnectionException {
        DriverConfig scyllaConfig = new ScyllaConfigBuilder()
                .setDriverName("ScyllaDriver")
                .addContactPoint(new ContactPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                .setAuthProvider(new PlainTextAuth("cassandra", "cassandra"))
                .setQueryCache(null)
                .build();

        ScyllaConnection connection = new ScyllaCluster(scyllaConfig)
                .connectSession("main");

        MessageApplication<Session> messageApplication = new MessageApplication<>(connection);

        long loggedId = 548102399621857300L;

        Channel mainChannel = new Channel(1249761829889052672L);

        messageApplication.sendAsync(mainChannel, loggedId, "Hello World !").thenAccept(messageSchema -> {
            logger.info("Message sent ! ID: " + messageSchema.getPrimaryValue());
        }).exceptionally(throwable -> {
            throwable.printStackTrace();

            return null;
        });

        Iterator<MessageSchema> iterator = messageApplication.getMessagesInBucket(mainChannel, 4966109);

        logger.info("Found messages !");

        while (iterator.hasNext()) {
            MessageSchema message = iterator.next();

            logger.info(message.toString());
        }

        Monitoring monitoring = new Monitoring("Get Content");
        messageApplication.getMessageRepository().getContent(1249762319070728192L).thenAccept(s -> {
            monitoring.done();
            logger.info("Content: " + s);
        });

    }

    public CompletableFuture<MessageSchema> sendAsync(Channel channel, long authorId, String content) {
        long messageId = snowflake.nextId();

        return messageRepository.insert(
                messageId,
                makeBucket(messageId),
                channel.getChannelId(),
                authorId,
                content
        );
    }

    public Iterator<MessageSchema> getMessagesInBucket(Channel channel, int bucket) {
        return messageRepository.findAllByBucket(bucket, channel.getChannelId());
    }

    public int makeBucket(long snowflake) {
        final long timestamp;

        if (snowflake == -1) {
            timestamp = System.currentTimeMillis() - EPOCH;
        } else {
            timestamp = snowflake >> 22;
        }

        return (int) (timestamp / BUCKET_SIZE);
    }

    public IntStream makeBuckets(long start_id, long end_id) {
        return IntStream.range(makeBucket(start_id), makeBucket(end_id) + 1);
    }

    public static MessageApplication<?> getInstance() {
        return INSTANCE;
    }

    public DriverSession<S> getDriverSession() {
        return driverSession;
    }

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    public Snowflake getSnowflake() {
        return snowflake;
    }

    public long getNodeId() {
        return nodeId;
    }


}
