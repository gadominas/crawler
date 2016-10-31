#!/bin/bash

./runParrallel.sh \
  "./openCrawlerSession.sh $1" \
  "./updateCrawlerSession.sh $1" \
  "./updateCrawlerSession.sh $1" \
  "./updateCrawlerSession.sh $1" \
  "./openCrawlerSession.sh $1" \
  "./updateCrawlerSession.sh $1" \
  "./updateCrawlerSession.sh $1" \
  "./updateCrawlerSession.sh $1" \
  "./updateCrawlerSession.sh $1" \
  "./closeCrawlerSession.sh $1"
