FROM clojure:alpine

RUN mkdir /atomix
COPY target/uberjar/atomix-playground-standalone.jar /atomix

WORKDIR /atomix

ENV PORT=5000
ENV MODE=bootstrap

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "atomix-playground-standalone.jar"]
