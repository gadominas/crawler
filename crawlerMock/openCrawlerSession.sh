#!/bin/bash
curl -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "$1" "$2/openCrawlerSession"
