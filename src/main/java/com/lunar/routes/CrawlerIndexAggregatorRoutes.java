package com.lunar.routes;

import com.lunar.cache.CrawlerIndexRepository;
import com.lunar.domain.CrawlIndexEntity;
import com.lunar.domain.CrawlSessionEntity;
import com.lunar.domain.dto.CrawlIndexPatch;
import com.lunar.domain.dto.CrawlerIndexExchange;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Aggregation & resuencencer route.
 * All crawl indexes are consumed & aggregated based on crawling URL async.
 * Aggregation stops when repoKey is provided.
 * After aggregation phase, crawl indexes are sorted by tx time (nanosec) and persisted sequentially
 * <p>
 * Created by gadominas on 10/30/16.
 */
@Component
public class CrawlerIndexAggregatorRoutes extends RouteBuilder {
    public final static String PROP_REPO_KEY = "repoKey";
    private final static String PROP_URL = "url";

    @Value("${crawIndexAggregateRoute.index}")
    private String crawIndexAggregateRouteIndex;

    @Value("${crawIndexAggregateRoute.asyncIndex}")
    private String crawIndexAggregateRouteAsyncIndex;

    @Autowired
    CrawlerIndexRepository crawlerIndexRepository;

    @Override
    public void configure() throws Exception {
        from(crawIndexAggregateRouteIndex).
                routeId(crawIndexAggregateRouteIndex).
                to(crawIndexAggregateRouteAsyncIndex).
                log(LoggingLevel.INFO, "+ Crawler index exchange consumed under: \"${body.getUrl.toString}.\"");


        from(crawIndexAggregateRouteAsyncIndex).
                routeId(crawIndexAggregateRouteAsyncIndex).
                process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        CrawlerIndexExchange crawlerIndexExchange = exchange.getIn().getBody(CrawlerIndexExchange.class);
                        Optional<String> repoKey = Optional.ofNullable(crawlerIndexExchange.getRepoKey());

                        if (repoKey.isPresent()) {
                            exchange.setProperty(PROP_REPO_KEY, repoKey.get());
                        }

                        exchange.setProperty(PROP_URL, crawlerIndexExchange.getUrl().toString());
                        exchange.setOut(exchange.getIn());
                    }
                }).
                aggregate(exchangeProperty(PROP_URL), new CrawlerIndexAggregationStrategy()).
                completionPredicate(new Predicate() {
                    @Override
                    public boolean matches(Exchange exchange) {
                        return StringUtils.isNotBlank((String) exchange.getProperty(PROP_REPO_KEY));
                    }
                }).
                process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        //dumpExchange(exchange);
                        exchange.setOut(exchange.getIn());
                    }
                }).
                log(LoggingLevel.INFO,
                        "++ Crawling session for ${exchangeProperty.CamelAggregatedCorrelationKey} is completed. " +
                                "Crawled repo aggregated size: ${exchangeProperty.CamelAggregatedSize} ++").
                split(body()).parallelProcessing(false).
                resequence(simple("${body.getTxDate}")).
                process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        // persist crawler indexes sequentially based on tx date.
                        CrawlerIndexExchange crawlerIndexExchange = exchange.getIn().getBody(CrawlerIndexExchange.class);
                        CrawlIndexEntity crawlIndexEntity = crawlerIndexRepository.getByUrl(crawlerIndexExchange.getUrl());

                        Optional<CrawlIndexPatch> crawlIndexPatchOptional = Optional.ofNullable(crawlerIndexExchange.getCrawlIndexPatch());

                        if (crawlIndexPatchOptional.isPresent()) {
                            CrawlSessionEntity crawlSessionEntity = new CrawlSessionEntity();
                            crawlSessionEntity.setTxDate(crawlerIndexExchange.getTxDate());
                            crawlSessionEntity.setRepoKey(exchange.getProperty(PROP_REPO_KEY).toString());
                            crawlSessionEntity.setPersons(crawlIndexPatchOptional.get().getPersons());

                            crawlIndexEntity.getCrawlSessionEntities().add(crawlSessionEntity);
                        }
                    }
                });
    }

    private static void dumpExchange(Exchange exchange) {
        System.out.println("exchange.getProperties");
        exchange.getProperties().
                forEach((k, v) ->
                        System.out.println("Item : " + k + " Value : " + v));
    }
}