FROM anapsix/alpine-java:jre8

ADD https://github.com/just-containers/s6-overlay/releases/download/v1.11.0.1/s6-overlay-amd64.tar.gz /tmp/

RUN apk upgrade --update && \
    apk add --update libgcc libstdc++ bash bash-doc bash-completion && \
    tar xzf /tmp/s6-overlay-amd64.tar.gz -C / && \
    rm -rf /tmp/* /var/cache/apk/*

COPY scripts/run.sh /opt/atomix/run.sh

COPY target/uberjar/replica.jar /opt/atomix/replica.jar

ENTRYPOINT ["/init"]

EXPOSE 5000

VOLUME ["/opt/atomix/logs"]

CMD ["opt/atomix/run.sh"]
