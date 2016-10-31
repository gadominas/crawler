package com.lunar.domain.dto;

import java.net.URL;

/**
 * Crawler exchange entity used for aggregation
 * Created by gadominas on 10/31/16.
 */
public class CrawlerIndexExchange {
    private URL url;
    private Long txDate;
    private String repoKey;

    private CrawlIndexPatch crawlIndexPatch;

    public CrawlerIndexExchange(URL url){
        txDate = System.nanoTime();
        setUrl(url);
    }

    public Long getTxDate() {
        return txDate;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getRepoKey() {
        return repoKey;
    }

    public void setRepoKey(String repoKey) {
        this.repoKey = repoKey;
    }

    public CrawlIndexPatch getCrawlIndexPatch() {
        return crawlIndexPatch;
    }

    public void setCrawlIndexPatch(CrawlIndexPatch crawlIndexPatch) {
        this.crawlIndexPatch = crawlIndexPatch;
    }
}
