# WAL crawler
Write-ahead-log crawler baked with SpringBoot/Guava/Camel.

# Happy path prolog
* Crawler opens crawling session for a given URL. Crawls person profiles for a given URL and then closes crawling session.
* Crawled data is kept in the mem index cache until crawling session is closed with the given repo-key.
* When crawling session is closed, aggregated crawling data is sorted asc by crawling transaction date and 'persisted' sequentially to a separate cache repo.
* Crawled data for a closed crawling session can retried.
* Crawling session can be reopened for the same given URL which would result in a populating existing crawling index for a given URL.
* When crawling session is closed, index cache is evicted for a given URL.
* Crawling data consumption and crawled session crawl data persistence are async processes.

# Baking recipes
## Clone & package
```
git clone https://github.com/gadominas/crawler.git
cd crawler/ && ./run.sh
```
If you're lucky after that bake you should be able to access swagger ui at: http://localhost:8080/swagger-ui.html

## Docker desert
To bake docker image:
```
mvn package docker:build
```

and run the docker container:
```
docker run -p 8080:8080 -t crawler/crawler
```

# Test drive
```
cd crawlerMock/ && ./run.sh http://localhost:8080
```


# Available crawler services
Swagger like documentation available at: http://${your_host}:8080/swagger-ui.html

## (1) /openCrawlerSession - Open crawling session for a specific URL
Crawler open crawling session for a given URL. In case the URL was already transmitted, warning msg is send.

```
curl -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "http://ccc.de" "http://localhost:8080/openCrawlerSession"
```

Possible metrics:
*  "counter.status.200.openCrawlerSession"
*  "counter.status.201.openCrawlerSession"

## (2) /updateCrawlerSession - Update crawling session with the crawled person profiles
Crawler consumes person profiles for a specific URL. An unknown URL is any URL which was not received in call #1 and and will result in error.

```
curl -X PATCH --header "Content-Type: application/json" --header "Accept: */*" -d "{
  \"persons\": [
    {
      \"dob\": \"2016-10-31T17:32:09.986Z\",
      \"firstname\": \"string\",
      \"lastname\": \"string\"
    }
  ],
  \"url\": \"http://ccc.de\"
}" "
```

Possible metrics:
*  "counter.status.422.updateCrawlerSession"
*  "counter.status.200.updateCrawlerSession"


## (3) /closeCrawlerSession/{repoKey} - Finalize crawling session for a particular URL by providing a repository key
Crawler consumes remote repository key (string) for a specific URL. Unknown URL results in error. This is the last modification call for the job and marks the end of it after witch crawled session is persisted.

```
curl -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "http://ccc.de" "http://localhost:8080/closeCrawlerSession/42"
```

Possible metrics:
*  "counter.status.422.closeCrawlerSession.repoKey"
*  "counter.status.200.closeCrawlerSession.repoKey"


## /getOpenCrawlers - Retrieve all WIP crawing sessions
Crawler produce all available information about given URL - a current list of peoples profiles and the repository key.

```
curl -X GET --header "Accept: */*" "http://localhost:8080/getOpenCrawlers"
```

Possible metrics:
*  "counter.status.200.getOpenCrawlers"
*  "counter.status.404.getOpenCrawlers"

## /getPersonProfilesByURL - Retrieve all crawled person profiles for a given URL
Crawler might ask to retrieve any jobs, which are not finished at the moment. An unfinished job stands for any URL, which has not received any call #1 or #3.

```
curl -X GET --header "Content-Type: application/json" --header "Accept: */*" -d "http://ccc.de" "http://localhost:8080/getPersonProfilesByURL"
```

Possible metrics:
*  "counter.status.200.getPersonProfilesByURL"
*  "counter.status.404.getPersonProfilesByURL"

## Health check on a crawler host

```
curl -X GET --header "Accept: application/json" "http://localhost:8080/health"
```

## Metrics API

Various metrics can be retrieved via /metrics/{name}:

```
curl -X GET --header "Accept: application/json" "http://localhost:8080/metrics/counter.status.422.updateCrawlerSession"
```


