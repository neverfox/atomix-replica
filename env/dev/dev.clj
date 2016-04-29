(ns dev
  (:require
   [atomix-playground.core :as system]
   [clojure.tools.namespace.repl :refer [refresh]]))

(def system nil)

(defn init!
  "Constructs the system."
  [runtime-config]
  (alter-var-root #'system (constantly (system/system runtime-config))))

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
  [runtime-config]
  (init! runtime-config)
  (start!))

(defn restart!
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop!)
  (refresh :after `go!))
