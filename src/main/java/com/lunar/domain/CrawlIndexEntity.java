package com.lunar.domain;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gadominas on 10/31/16.
 */
public class CrawlIndexEntity {
    private URL url;
    private ArrayList<CrawlSessionEntity> crawlSessionEntities = new ArrayList<>();

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public ArrayList<CrawlSessionEntity> getCrawlSessionEntities() {
        return crawlSessionEntities;
    }

    public void setCrawlSessionEntities(ArrayList<CrawlSessionEntity> crawlSessionEntities) {
        this.crawlSessionEntities = crawlSessionEntities;
    }
}
