package com.lunar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by gadominas on 10/31/16.
 */
public abstract class CrawlerCache {
    @Autowired
    private CacheManager cacheManager;

    protected abstract String getCacheGroupName();

    /**
     * Check if CrawlerIndex is present in a cache without updating the cache
     * @param url
     * @return
     */
    public Boolean isPresent(URL url){
        GuavaCache guavaCache = getCacheManager();
        Optional<Object> optional = Optional.ofNullable(guavaCache.getNativeCache().getIfPresent(url));
        return optional.isPresent();
    }

    public ConcurrentMap<Object, Object> asMap(){
        GuavaCache guavaCache = getCacheManager();
        return guavaCache.getNativeCache().asMap();
    }

    private GuavaCache getCacheManager(){
        return (GuavaCache)cacheManager.getCache(getCacheGroupName());
    }
}
