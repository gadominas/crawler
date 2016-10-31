package com.lunar;

import com.lunar.domain.dto.CrawlIndexPatch;
import com.lunar.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by gadominas on 10/30/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrawlerControllerTests {
    private URL base;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    private URL urlBase(String path) throws MalformedURLException {
        return new URL("http://localhost:" + port + "/" + path);
    }

    @Test
    public void openCrawlerSession() throws Exception {
        final String urlToCrawl = "http://ccc.de";
        final URL urlBase = urlBase("openCrawlerSession");

        ResponseEntity<String> response = template.postForEntity(urlBase.toString(), urlToCrawl, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response = template.postForEntity(urlBase.toString(), urlToCrawl, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        response = template.postForEntity(urlBase.toString(), urlToCrawl+".com", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void testFullCrawlingCycle() throws Exception {
        final String urlToCrawl = "http://ccc2.de";
        final URL urlOpenCrawlerSession = urlBase("openCrawlerSession");
        final URL urlCloseCrawlerSession = urlBase("closeCrawlerSession/123");

        Person person = new Person();
        person.setFirstname("John");
        person.setFirstname("Doe");
        person.setDob(new Date());

        CrawlIndexPatch crawlIndexPatch = new CrawlIndexPatch();
        crawlIndexPatch.getPersons().add(person);
        crawlIndexPatch.setUrl(urlToCrawl);

        ResponseEntity<String> response = template.postForEntity(urlOpenCrawlerSession.toString(), urlToCrawl, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response = template.postForEntity(urlCloseCrawlerSession.toString(), urlToCrawl, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }
}
