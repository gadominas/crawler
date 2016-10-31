package com.lunar.routes;

import com.lunar.domain.dto.CrawlerIndexExchange;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Custom Crawler index aggregator strategy for combining exchange messages to list based on correlation key
 * Created by gadominas on 10/31/16.
 */
public class CrawlerIndexAggregationStrategy implements AggregationStrategy {

    public CrawlerIndexAggregationStrategy() {
        super();
    }

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Message newIn = newExchange.getIn();
        Object newBody = newIn.getBody();

        Optional<Exchange> exchangeOptional = Optional.ofNullable(oldExchange);

        ArrayList list = null;
        if (!exchangeOptional.isPresent()) {
            list = new ArrayList();
            list.add(newBody);
            newIn.setBody(list);

            return newExchange;
        } else {
            Message in = exchangeOptional.get().getIn();
            list = in.getBody(ArrayList.class);
            list.add(newBody);

            // update repo key for a old exchange which will be checked at the aggregation completion predicate
            CrawlerIndexExchange crawlerIndexExchange = newExchange.getIn().getBody(CrawlerIndexExchange.class);
            Optional<String> repoKey = Optional.ofNullable(crawlerIndexExchange.getRepoKey());

            if (repoKey.isPresent()) {
                exchangeOptional.get().setProperty(CrawlerIndexAggregatorRoutes.PROP_REPO_KEY, repoKey.get());
            }

            return exchangeOptional.get();
        }
    }
}