FROM openjdk:8-jre-slim

VOLUME /tmp
ADD target/ethereummonitor-0.0.1-SNAPSHOT.jar ethereum-monitor.jar
RUN sh -c 'touch /ethereum-monitor.jar' && \
    mkdir config

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Duser.timezone=UTC -jar /ethereum-monitor.jar