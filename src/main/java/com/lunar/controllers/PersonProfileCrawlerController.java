package com.lunar.controllers;

import com.lunar.cache.CrawlerIndexCache;
import com.lunar.domain.dto.CrawlCacheIndex;
import com.lunar.domain.dto.CrawlIndexPatch;
import com.lunar.domain.dto.CrawlerIndexExchange;
import com.lunar.routes.CrawlerIndexAggregatorProducer;
import io.swagger.annotations.*;
import org.apache.camel.Produce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gadominas on 10/30/16.
 */
@RestController()
public class PersonProfileCrawlerController {
    @Autowired
    private CrawlerIndexCache crawlerIndexCache;

    @Autowired
    private CacheManager cacheManager;

    @Produce(uri = "direct:crawIndexAggregateRoute")
    private CrawlerIndexAggregatorProducer crawlerIndexAggregatorProducer;

    @ApiOperation(value = "Open crawling session for a specific URL")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "Crawler sends a source URL, which " +
                    "is about to be scanned", required = true, dataType = "string", paramType = "body")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Warning message in case URL maches already opened crawling session")})
    @RequestMapping(value = "openCrawlerSession", method = RequestMethod.POST)
    public ResponseEntity<String> openCrawlerSession(@RequestBody String url) throws MalformedURLException {
        URL urlToIndex = new URL(url);

        if (crawlerIndexCache.isPresent(urlToIndex)) {
            return new ResponseEntity<String>("{warnings:true, warning:'Crawling for: \"" + url +
                    "\" has been already started'}", HttpStatus.OK);
        } else {
            CrawlCacheIndex crawlIndex = crawlerIndexCache.getByUrl(urlToIndex);
            CrawlerIndexExchange crawlerIndexExchange = new CrawlerIndexExchange(urlToIndex);
            crawlerIndexAggregatorProducer.openCrawlerSession(crawlerIndexExchange);

            return new ResponseEntity<String>("Crawling for: " + url +
                    " was successfully started", HttpStatus.CREATED);
        }
    }

    @ApiOperation(value = "Update crawling session with the crawled person profiles")
    @ApiResponses(value = {
            @ApiResponse(code = 422, message = "No open crawling session is available for a provided URL")})
    @RequestMapping(value = "updateCrawlerSession", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateCrawlerSession(@RequestBody CrawlIndexPatch crawlIndexPatch) throws MalformedURLException {
        URL urlToIndex = new URL(crawlIndexPatch.getUrl());

        if (crawlerIndexCache.isPresent(urlToIndex)) {
            CrawlCacheIndex crawlIndex = crawlerIndexCache.getByUrl(urlToIndex);
            crawlIndex.getPersons().addAll(crawlIndexPatch.getPersons());

            CrawlerIndexExchange crawlerIndexExchange = new CrawlerIndexExchange(urlToIndex);
            crawlerIndexExchange.setCrawlIndexPatch(crawlIndexPatch);
            crawlerIndexAggregatorProducer.updateCrawlerSession(crawlerIndexExchange);

            return new ResponseEntity<String>("Crawled persons were associated to: " +
                    urlToIndex.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("No open crawling session found for: " +
                    urlToIndex.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @ApiOperation(value = "Finalize crawling session for a particular URL by providing a repository key")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "URL for which crawling session should be" +
                    " finalized", required = true, dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "repoKey", value = "Remote repository key (string) for a " +
                    "specific URL crowling session", required = true, dataType = "string", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 422, message = "No open crawling session is available for a provided URL")})
    @RequestMapping(value = "/closeCrawlerSession/{repoKey}", method = RequestMethod.POST)
    public ResponseEntity<String> closeCrawlerSession(@PathVariable String repoKey, @RequestBody String url) throws MalformedURLException {
        URL urlToIndex = new URL(url);

        if (crawlerIndexCache.isPresent(urlToIndex)) {
            crawlerIndexCache.updateRepoKey(urlToIndex, repoKey);

            CrawlerIndexExchange crawlerIndexExchange = new CrawlerIndexExchange(urlToIndex);
            crawlerIndexExchange.setRepoKey(repoKey);
            crawlerIndexAggregatorProducer.closeCrawlerSession(crawlerIndexExchange);

            return new ResponseEntity<String>("Crawling session was successfully closed for: " +
                    urlToIndex.toString() + " with repo key: " + repoKey, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("No open crawling session found for: " +
                    urlToIndex.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}