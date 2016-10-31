#!/bin/bash

index=$(echo $(( RANDOM % (99999) )))
curl -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "$1" "http://localhost:8080/closeCrawlerSession/${index}"
