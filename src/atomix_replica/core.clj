(ns atomix-replica.core
  (:gen-class)
  (:require
   [aero.core :refer [read-config]]
   [atomix-replica.replica :refer [map->ReplicaComponent]]
   [atomix-replica.seed :refer [map->SeedComponent]]
   [atomix-replica.storage :refer [map->StorageComponent]]
   [atomix-replica.transport :refer [map->TransportComponent]]
   [beckon]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log]
   [hara.component :as component]))

(defn system [runtime-config]
  (component/system
   {:replica   [map->ReplicaComponent :storage :transport :cluster]
    :storage   [map->StorageComponent]
    :transport [map->TransportComponent]
    :seed      [map->SeedComponent]
    :cluster   [{:expose [:cluster]} :seed]}
   runtime-config))

(defn stop [system]
  (beckon/reinit-all!)
  (log/info "<- Stopping system")
  (component/stop system))

(defn start [system]
  (log/info "-> Starting system")
  (let [started (component/start system)]
    (doseq [sig ["INT" "TERM"]]
      (reset! (beckon/signal-atom sig) #{(partial stop started)}))
    started))

(defn -main
  [& args]
  (start (system (-> "config.edn"
                     io/resource
                     read-config
                     (update-in [:replica :port] read-string)
                     (update-in [:replica :mode] keyword)
                     (update-in [:replica :session-timeout] read-string)
                     (update-in [:seed :provider] keyword))))
  @(promise))
