package ch.twidev.invodb.examples.caching;

import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.driver.redis.RedisCacheDriver;
import redis.clients.jedis.Jedis;

import java.beans.Transient;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

        QueryCache<Jedis> queryCache = new QueryCache<>(
              redisCacheDriver,
              CachingStrategy.LRU,
              CACHE_KEY,
              5
        );

        redisCacheDriver.cleanup(CACHE_KEY);

        ElementSet<?> elementSet = new ElementSetImpl("Salut","Hello World","Luca !");

        queryCache.put(30, elementSet);

        ElementSet<?> set = queryCache.get(30);
        System.out.println(set.toString());
        while (set.hasNext()) {
            Elements elements = set.next();
            System.out.println(elements.getObject(""));
        }
    }

    public static class ElementSetImpl extends ElementSet<String[]> {

        private final String[] elements;

        public ElementSetImpl(String... elements) {
            super(Arrays.stream(elements)
                    .map(ElementsImpl::new)
                    .map(Elements.class::cast)
                    .iterator(), elements);

            this.elements = elements;
        }

        @Override
        public boolean isEmpty() {
            return elements.length == 0;
        }

        @Override
        public Elements first() {
            return this.next();
        }

        @Override
        public ElementSet<String[]> fromElements() {
            System.out.println("Test"  + Arrays.toString(this.getElements()));

            return new ElementSetImpl(this.getElements());
        }

        @Override
        public long getTime() {
            return 0;
        }

        @Override
        public String toString() {
            return "ElementSetImpl{" +
                    "elements=" + Arrays.toString(elements) +
                    '}';
        }
    }

    public static class ElementsImpl implements Elements {

        private final String value;

        public ElementsImpl(String value) {
            this.value = value;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public Object getObject(String name) {
            return value;
        }

        @Override
        public <T> T getObject(String name, Class<T> type) {
            return (T) value;
        }

        @Override
        public <T> T getObject(int id, Class<T> type) {
            return (T) value;
        }
    }
}
