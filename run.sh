export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
mvn clean compile package && java -jar target/crawler-0.0.1-SNAPSHOT.jar
