(ns dev
  (:require
   [aero.core :refer [read-config]]
   [atomix-replica.core :as system]
   [clojure.tools.namespace.repl :refer [refresh]]
   [clojure.java.io :as io]))

(def system nil)

(def default-config
  (-> "config.edn"
      io/resource
      read-config
      (update-in [:replica :port] read-string)
      (update-in [:replica :mode] keyword)
      (update-in [:seed :provider] keyword)))

(defn init!
  "Constructs the system."
  ([] (init! default-config))
  ([runtime-config]
   (alter-var-root #'system (constantly (system/system runtime-config)))))

(defn start!
  "Starts the system."
  []
  (alter-var-root #'system system/start))

(defn stop!
  "Shuts down and destroys the system."
  []
  (alter-var-root #'system (fn [s] (when s (system/stop s)))))

(defn go!
  "Initializes the system and starts it running."
  ([] (go! default-config))
  ([runtime-config]
   (init! runtime-config)
   (start!)))

(defn restart!
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop!)
  (refresh :after `go!))
