USE main;

CREATE TABLE IF NOT EXISTS messages (
                          channel_id bigint,
                          bucket int,
                          message_id bigint,
                          author_id bigint,
                          content text,
                          PRIMARY KEY ((channel_id, bucket), message_id)
) WITH CLUSTERING ORDER BY (message_id DESC);