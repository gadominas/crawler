package com.lunar.cache;

import com.lunar.domain.CrawlIndexEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * Created by gadominas on 10/31/16.
 */
@Service
public class CrawlerIndexRepository extends CrawlerCache{
    public final static String CACHE_CRAWLERINDEX_REPO ="crawlerIndexRepo";

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = CACHE_CRAWLERINDEX_REPO)
    public CrawlIndexEntity getByUrl(URL url) {
        CrawlIndexEntity crawlIndexEntity = new CrawlIndexEntity();
        crawlIndexEntity.setUrl(url);

        return crawlIndexEntity;
    }

    @Override
    protected String getCacheGroupName() {
        return CACHE_CRAWLERINDEX_REPO;
    }
}
