(ns atomix-playground.replica
  (:require
   [clojure.tools.logging :as log]
   [hara.component :refer [IComponent]])
  (:import
   [io.atomix AtomixReplica]
   [io.atomix.catalyst.transport Address]
   [io.atomix.catalyst.transport.netty NettyTransport]
   [io.atomix.copycat.server.storage Storage]
   [java.net InetAddress]))

(defrecord ReplicaComponent []
  IComponent
  (-start [{:keys [host port mode cluster] :as self}]
    (log/info "-> Starting replica")
    (let [localhost     (-> (InetAddress/getLocalHost)
                            (.getHostAddress))
          local-address (Address. (or host localhost) port)
          storage       (get-in self [:storage :storage] (Storage.))
          transport     (get-in self [:transport :transport] (NettyTransport.))
          replica       (.. (AtomixReplica/builder local-address)
                            (withTransport transport)
                            (withStorage storage)
                            build)]
      (if cluster
        (condp = mode
          :bootstrap @(.bootstrap replica cluster)
          :join      @(.join replica cluster))
        @(.bootstrap replica))
      (assoc self :replica replica)))
  (-stop [{:keys [replica] :as self}]
    (log/info "<- Stopping replica")
    @(.leave replica)
    (dissoc self :replica)))
