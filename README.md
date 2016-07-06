# atomix-replica

A standalone Atomix replica, implemented in Clojure, with Docker/Kubernetes support

## Usage

REPL:

    user> (dev)
    dev> (go!)
    dev> (restart!)
    dev> (stop!)

Standalone:

    $ docker build -t atomix .
    $ docker run -d atomix

Sample cluster (assumes [DNSDock](https://github.com/tonistiigi/dnsdock)):

    $ docker-compose up
    
Kubernetes:

    $ kubectl create -f atomix-service.yaml
    $ kubectl create -f atomix-bootstrap-deployment.yaml
    $ kubectl create -f atomix-join-deployment.yaml
    $ kubectl scale --replicas=3 -f atomix-join-deployment.yaml
    $ kubectl delete -f atomix-bootstrap-deployment.yaml

## License

Copyright © 2016 Roman Pearah

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
