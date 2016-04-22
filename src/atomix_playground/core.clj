(ns atomix-playground.core
  (:require
   [atomix-playground.replica :refer [map->Replica]]
   [hara.component :as component]
   [trinity.core :as trinity])
  (:import
   [io.atomix AtomixClient AtomixReplica]
   [io.atomix.catalyst.transport Address NettyTransport])
  (:gen-class))

(def system (component/system
             {:replica [map->Replica]}
             {:replica {:port 5000}}))

(def start component/start)

(def stop component/stop)

(defn -main
  [& args]
  (start)
  @(promise))
