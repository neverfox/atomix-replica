(ns atomix-playground.core
  (:require
   [atomix-playground.replica :refer [map->Replica]]
   [environ.core :refer [env]]
   [hara.component :as component])
  (:gen-class))

(defn system [runtime-config]
  (component/system
   {:replica [map->Replica]}
   runtime-config))

(def start component/start)

(def stop component/stop)

(defn -main
  [& args]
  (start (system {:replica {:port  (read-string (env :port))
                            :mode  (keyword (env :mode))
                            :nodes (if (env :nodes) (clojure.string/split (env :nodes) #","))}}))
  @(promise))
