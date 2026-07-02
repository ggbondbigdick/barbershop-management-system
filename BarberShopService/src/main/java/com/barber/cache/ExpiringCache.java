package com.barber.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: ExpiringCache
 * Description: This class is used to implement an expiring cache.
 */
public class ExpiringCache {

    private static class CacheValue {

        Object value;

        long expireTime; // 过期时间戳 (单位：毫秒) Expiration timestamp

        CacheValue(Object value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    private static final ConcurrentHashMap<String, CacheValue> CACHE = new ConcurrentHashMap<>();

    /**
     * 添加缓存（带过期时间）
     * add cache with expiration time
     * @param key
     * @param value
     * @param ttlMillis
     */
    public static void put(String key, Object value, long ttlMillis) {
        long expireTime = System.currentTimeMillis() + ttlMillis;
        CACHE.put(key, new CacheValue(value, expireTime));
    }


    /**
     * 获取缓存
     * get cache
     * @param key
     * @return
     */
    public static Object get(String key) {
        CacheValue cacheValue = CACHE.get(key);
        if (cacheValue == null || cacheValue.isExpired()) {
            CACHE.remove(key);
            return null; // 返回 null 表示缓存失效或不存在 Cache expired or does not exist
        }
        return cacheValue.value;
    }

    /**
     * 移除缓存
     * remove cache
     * @param key
     * @return
     */
    public static void remove(String key) {
        CACHE.remove(key);
    }


    /**
     * 清空缓存
     * clear cache
      */
    public static void clear() {
        CACHE.clear();
    }

}
