package com.lunar.routes;

import com.lunar.domain.dto.CrawlerIndexExchange;

/**
 * Created by gadominas on 10/30/16.
 */
public interface CrawlerIndexAggregatorProducer {
    public void openCrawlerSession(CrawlerIndexExchange crawlerIndexExchange);
    public void updateCrawlerSession(CrawlerIndexExchange crawlerIndexExchange);
    public void closeCrawlerSession(CrawlerIndexExchange crawlerIndexExchange);
}
