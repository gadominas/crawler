package com.lunar.cache;

import com.lunar.domain.dto.CrawlCacheIndex;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * Created by gadominas on 10/30/16.
 */
@Service
public class CrawlerIndexCache extends CrawlerCache {
    public final static String CACHE_CRAWLERINDEX="crawlerIndexCache";

    @Cacheable(value = CACHE_CRAWLERINDEX)
    public CrawlCacheIndex getByUrl(URL url) {
        CrawlCacheIndex crawlIndex = new CrawlCacheIndex();
        crawlIndex.setUrl(url);

        return crawlIndex;
    }

    @CacheEvict(value = CrawlerIndexCache.CACHE_CRAWLERINDEX, key = "#url")
    public CrawlCacheIndex updateRepoKey(URL url, String repoKey){
        CrawlCacheIndex crawlIndex = getByUrl(url);
        crawlIndex.setRepoKey(repoKey);

        return  crawlIndex;
    }

    @Override
    protected String getCacheGroupName() {
        return CACHE_CRAWLERINDEX;
    }
}
