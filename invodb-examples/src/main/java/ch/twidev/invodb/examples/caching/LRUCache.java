package ch.twidev.invodb.examples.caching;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.common.cache.StreamCacheProvider;
import ch.twidev.invodb.driver.redis.RedisCacheDriver;

public class LRUCache {

    public static void main(String[] args) {

        Cache<Integer, String> queryCache = new StreamCacheProvider<>(
                new RedisCacheDriver("45.13.119.231", 6379, null),
                CachingStrategy.LRU,
                "test",
                10
        ){};

        queryCache.put(31, "Hello World!");
        System.out.println(queryCache.get(23));
    }

}
