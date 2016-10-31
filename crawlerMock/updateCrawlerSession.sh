#!/bin/bash

index=$(echo $(( RANDOM % (99999) )))
curl -X PATCH --header "Content-Type: application/json" --header "Accept: */*" -d "{
  \"persons\": [
    {
      \"firstname\": \"John_${index}\",
      \"lastname\": \"Doe_${index}\"
    }
  ],
  \"url\": \"$1\"
}" "$2/updateCrawlerSession"
