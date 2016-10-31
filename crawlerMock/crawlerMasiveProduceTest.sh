#!/bin/sh

start_time=`date +%s`

./runParrallel.sh \
  "./crawlerMockInstance.sh http://facebook.com" \
  "./crawlerMockInstance.sh http://ccc.de" \
  "./crawlerMockInstance.sh http://cnn.com" \
  "./crawlerMockInstanceNoClose.sh http://facebook.com" \
  "./crawlerMockInstance.sh http://twitter.com" \
  "./crawlerMockInstance.sh http://hak5.com" \

end_time=`date +%s`
echo execution time was `expr $end_time - $start_time` s.
