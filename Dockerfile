FROM phusion/baseimage

RUN apt-get update
RUN apt-get -y -q install software-properties-common
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get -y -q update
RUN echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | sudo debconf-set-selections
RUN apt-get install -qqy oracle-java8-installer oracle-java8-set-default wget
RUN rm -rf /var/lib/apt/lists/* && rm -rf /var/cache/oracle-jdk8-installer

RUN mkdir -p /atomix/logs && cd /atomix && wget -O atomix-standalone-server.jar http://search.maven.org/remotecontent?filepath=io/atomix/atomix-standalone-server/1.0.0-rc4/atomix-standalone-server-1.0.0-rc4-shaded.jar

VOLUME /atomix/logs
