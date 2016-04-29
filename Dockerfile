FROM 311873742948.dkr.ecr.us-east-1.amazonaws.com/demandsignals/baseimage-jvm

RUN cd / && git clone -b develop http://github.com/neverfox/atomix-playground /atomix

ENV PORT=5000
ENV MODE=bootstrap

EXPOSE 5000

WORKDIR /atomix
ENTRYPOINT ["/usr/bin/lein", "run"]
