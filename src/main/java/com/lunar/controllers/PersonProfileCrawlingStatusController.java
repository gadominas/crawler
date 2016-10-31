package com.lunar.controllers;

import com.lunar.cache.CrawlerIndexCache;
import com.lunar.cache.CrawlerIndexRepository;
import com.lunar.domain.CrawlIndexEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by gadominas on 10/31/16.
 */
@RestController()
public class PersonProfileCrawlingStatusController {
    @Autowired
    private CrawlerIndexCache crawlerIndexCache;

    @Autowired
    private CrawlerIndexRepository crawlerIndexRepository;

    @ApiOperation(value = "Retrieve all WIP crawing sessions")
    @RequestMapping(value = "getOpenCrawlers", method = RequestMethod.GET)
    public ResponseEntity<ConcurrentMap<Object, Object>> getOpenCrawlers() throws MalformedURLException {
        if( !crawlerIndexCache.asMap().isEmpty() ){
            return new ResponseEntity<ConcurrentMap<Object, Object>>(crawlerIndexCache.asMap(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ConcurrentMap<Object, Object>>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Retrieve all crawled person profiles for a given URL")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", required = true, dataType = "string",  paramType = "body")
    })
    @RequestMapping(value = "getPersonProfilesByURL", method = RequestMethod.GET)
    public ResponseEntity<CrawlIndexEntity> getPersonProfilesByURL(@RequestBody String url) throws MalformedURLException {
        URL urlToFetch = new URL(url);

        if( crawlerIndexRepository.isPresent(urlToFetch) ){
            System.out.println(ReflectionToStringBuilder.toString(crawlerIndexRepository.getByUrl(urlToFetch)));
            return new ResponseEntity<CrawlIndexEntity>(crawlerIndexRepository.getByUrl(urlToFetch), HttpStatus.OK);
        } else {
            return new ResponseEntity<CrawlIndexEntity>(HttpStatus.NOT_FOUND);
        }
    }
}
