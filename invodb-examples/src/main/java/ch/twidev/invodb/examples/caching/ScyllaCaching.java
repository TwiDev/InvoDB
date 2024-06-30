package ch.twidev.invodb.examples.caching;

import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.bridge.cache.MapCache;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.bridge.driver.auth.PlainTextAuth;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.common.cache.MapCacheProvider;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.cache.SchemaCacheProvider;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.common.util.Monitoring;
import ch.twidev.invodb.driver.redis.RedisCacheDriver;
import ch.twidev.invodb.driver.scylla.ScyllaCluster;
import ch.twidev.invodb.driver.scylla.ScyllaConfigBuilder;
import ch.twidev.invodb.driver.scylla.ScyllaConnection;
import org.redisson.api.RedissonClient;

import java.net.InetSocketAddress;
import java.util.UUID;

public class ScyllaCaching {

    public static void main(String[] args) throws DriverConnectionException {
        RedisCacheDriver redisCacheDriver = new RedisCacheDriver("45.13.119.231", 6379, null);

        final String CACHE_KEY = "scylladbTest";

        QueryCache<RedissonClient> queryCache = new QueryCache<>(
                redisCacheDriver,
                CachingStrategy.LRU,
                CACHE_KEY,
                5
        );

        DriverConfig scyllaConfig = new ScyllaConfigBuilder()
                .setDriverName("ScyllaDriver")
                .addContactPoint(new ContactPoint(InetSocketAddress.createUnresolved("45.13.119.231", 9042)))
                .setAuthProvider(new PlainTextAuth("cassandra", "cassandra"))
                .setQueryCache(queryCache)
                .build();

        ScyllaConnection connection = new ScyllaCluster(scyllaConfig)
                .connectSession("main");

        Monitoring monitoring = new Monitoring("Find Op");
        ElementSet<?> result = InvoQuery.find("users")
                .where(SearchFilter.eq("name","Hello"))
                .run(connection);
        monitoring.done();

        System.out.println("Cached ? : " + result.isCached() + " | " + result.getClass());

        while (result.hasNext()) {
            Elements elements = result.next();

            System.out.println(elements.getObject("email"));
        }

        EvictionPolicy<String, RedissonClient> evictionPolicy = redisCacheDriver.getEvictionPolicy(CachingStrategy.LRU, "users", 5);

        for (int i = 0; i < 3; i++) {
            MapCache<String> mapCache = new MapCacheProvider<>(
                    redisCacheDriver,
                    evictionPolicy,
                    UUID.randomUUID().toString()
            );

            mapCache.put("test", "Hello");
            mapCache.put("test2", 345);

            System.out.println(
                    mapCache.getString("test") + " UUID: " + mapCache.getKeyname()
            );

            System.out.println(
                    mapCache.getInt("test2")
            );
        }

    }

}
