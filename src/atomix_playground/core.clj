(ns atomix-playground.core
  (:require
   [beckon]
   [atomix-playground.replica :refer [map->ReplicaComponent]]
   [atomix-playground.storage :refer [map->StorageComponent]]
   [atomix-playground.transport :refer [map->TransportComponent]]
   [environ.core :refer [env]]
   [hara.component :as component]
   [taoensso.timbre :refer [refer-timbre]])
  (:gen-class))

(refer-timbre)

(defn system [runtime-config]
  (component/system
   {:replica [map->ReplicaComponent :storage :transport]
    :storage [map->StorageComponent]
    :transport [map->TransportComponent]}
   runtime-config))

(defn stop [system]
  (beckon/reinit-all!)
  (info "<- Stopping system")
  (component/stop system))

(defn start [system]
  (info "-> Starting system")
  (let [started (component/start system)]
    (doseq [sig ["INT" "TERM"]]
      (reset! (beckon/signal-atom sig) #{(partial stop started)}))
    started))

(defn -main
  [& args]
  (start (system {:replica {:port  (read-string (env :port))
                            :mode  (keyword (env :mode))
                            :nodes (if (env :nodes) (clojure.string/split (env :nodes) #","))}
                  :storage {:level :disk}}))
  @(promise))
