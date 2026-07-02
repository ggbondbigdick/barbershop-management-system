package com.barber.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: GlobalCache
 * Description: global cache class
 */
public class GlobalCache {

    private static final ConcurrentHashMap<String, Object> CACHE = new ConcurrentHashMap<>();


    /**
     * add cache
     * @param key key
     * @param value value
     * @return value
     */
    public static void put(String key, Object value) {
        CACHE.put(key, value);
    }


    /**
     * get cache
     * @param key key
     * @return value
     */
    public static Object get(String key) {
        return CACHE.get(key);
    }


    /**
     * remove cache
     * @param key key
     */
    public static void remove(String key) {
        CACHE.remove(key);
    }


    /**
     * clear cache
     */
    public static void clear() {
        CACHE.clear();
    }


    /**
     * contains key
     * @param key key
     * @return true or false
     */
    public static boolean containsKey(String key) {
        return CACHE.containsKey(key);
    }


    /**
     * get cache size
     * @return cache size
     */
    public static int size() {
        return CACHE.size();
    }
}
