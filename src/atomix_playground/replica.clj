(ns atomix-playground.replica
  (:require
   [hara.component :refer [IComponent]])
  (:import
   [io.atomix AtomixReplica]
   [io.atomix.catalyst.transport Address NettyTransport]
   [io.atomix.copycat.server.storage Storage]
   [java.net InetAddress]))

(defrecord Replica []
  IComponent
  (-start [{:keys [port] :as self}]
    (let [localhost (-> (InetAddress/getLocalHost)
                        (.getHostName))
          local-address (Address. localhost port)
          storage (get self :storage (Storage.))
          transport (get self :transport (NettyTransport.))
          replica (.. (AtomixReplica/builder local-address)
                      (withTransport transport)
                      (withStorage storage)
                      build)]
      @(.bootstrap replica)
      (assoc self :replica replica)))
  (-stop [{:keys [replica] :as self}]
    @(.shutdown replica)
    @(.leave replica)
    (dissoc self :replica)))
