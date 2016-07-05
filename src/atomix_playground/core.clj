(ns atomix-playground.core
  (:require
   [aero.core :refer [read-config]]
   [atomix-playground.replica :refer [map->ReplicaComponent]]
   [atomix-playground.seed :refer [map->SeedComponent]]
   [atomix-playground.storage :refer [map->StorageComponent]]
   [atomix-playground.transport :refer [map->TransportComponent]]
   [beckon]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log]
   [hara.component :as component])
  (:gen-class))

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
  (start (system (read-config (io/resource "config.edn"))))
  @(promise))
