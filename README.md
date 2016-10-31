# WAL crawler
Write-ahead-log crawler baked with SpringBoot/Guava.

# Available crawler services
## (1) /openCrawlerSession - Open crawling session for a specific URL
Crawler open crawling session for a given URL. In case the URL was already transmitted, warning msg is send.

```
curl -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "http://ccc.de" "http://localhost:8080/openCrawlerSession"
```

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

## (3) /closeCrawlerSession/{repoKey} - Finalize crawling session for a particular URL by providing a repository key
Crawler consumes remote repository key (string) for a specific URL. Unknown URL results in error. This is the last modification call for the job and marks the end of it after witch crawled session is persisted.

```
curl -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "http://ccc.de" "http://localhost:8080/closeCrawlerSession/42"
```

## /getOpenCrawlers - Retrieve all WIP crawing sessions
Crawler produce all available information about given URL - a current list of peoples profiles and the repository key.

```
curl -X GET --header "Accept: */*" "http://localhost:8080/getOpenCrawlers"
```

## /getPersonProfilesByURL - Retrieve all crawled person profiles for a given URL
Crawler might ask to retrieve any jobs, which are not finished at the moment. An unfinished job stands for any URL, which has not received any call #1 or #3.

```
curl -X GET --header "Content-Type: application/json" --header "Accept: */*" -d "http://ccc.de" "http://localhost:8080/getPersonProfilesByURL"
```

