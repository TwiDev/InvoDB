package ch.twidev.invodb.examples.caching;

import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.common.cache.AsyncQueryCache;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.driver.redis.RedisCacheDriver;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.beans.Transient;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class LRUCache {

    public static void main(String[] args) {

        RedisCacheDriver redisCacheDriver = new RedisCacheDriver("45.13.119.231", 6379, null);

        final String CACHE_KEY = "test";

       /* Cache<Integer, String> queryCache = new StreamCacheProvider<>(
                redisCacheDriver,
                CachingStrategy.LRU,
                CACHE_KEY,
                20
        ){};

        redisCacheDriver.cleanup("test");

        long l = System.nanoTime();

        for (int i = 100; i < 250; i++) {
            queryCache.put(i, i + "item !");
        }

        System.out.println("(249) " + queryCache.get(249));

        System.out.println((System.nanoTime() - l) / 1_000_000);*/

        AsyncQueryCache<RedissonClient> queryCache = new AsyncQueryCache<>(
              redisCacheDriver,
              CachingStrategy.LRU,
              CACHE_KEY,
              5
        );


    }


}
