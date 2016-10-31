#!/bin/sh

start_time=`date +%s`

./runParrallel.sh \
  "./crawlerMockInstance.sh http://facebook.com $1" \
  "./crawlerMockInstance.sh http://ccc.de $1" \
  "./crawlerMockInstance.sh http://cnn.com $1" \
  "./crawlerMockInstanceNoClose.sh http://facebook.com $1" \
  "./crawlerMockInstance.sh http://twitter.com $1" \
  "./crawlerMockInstance.sh http://hak5.com $1" \
  "./crawlerMockInstance.sh http://1.com $1" \
  "./crawlerMockInstanceNoClose.sh http://wikileaks.org $1" \
  "./crawlerMockInstance.sh http://wikileaks.org $1" \
  "./crawlerMockInstance.sh https://facebookcorewwwi.onion $1" \

end_time=`date +%s`
echo execution time was `expr $end_time - $start_time` s.
