#!/bin/bash

./runParrallel.sh \
  "./openCrawlerSession.sh $1 $2" \
  "./updateCrawlerSession.sh $1 $2" \
  "./updateCrawlerSession.sh $1 $2" \
  "./updateCrawlerSession.sh $1 $2" \
  "./openCrawlerSession.sh $1 $2" \
  "./updateCrawlerSession.sh $1 $2" \
  "./updateCrawlerSession.sh $1 $2" \
  "./updateCrawlerSession.sh $1 $2" \
  "./updateCrawlerSession.sh $1 $2" \
  "./closeCrawlerSession.sh $1 $2"
