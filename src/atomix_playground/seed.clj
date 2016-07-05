(ns atomix-playground.seed
  (:require
   [clojure.core.async :refer [<!!]]
   [clojure.tools.logging :as log]
   [hara.component :refer [IComponent]]
   [kubernetes.api.v1 :as k8s])
  (import
   [io.atomix.catalyst.transport Address]))

(defn make-cluster [coll]
  (mapv #(Address. %) coll))

(defmulti get-nodes :provider :default :none)

(defmethod get-nodes :none [_] nil)

(defmethod get-nodes :local [{:keys [nodes]}] (make-cluster nodes))

(defmethod get-nodes :k8s
  [{:keys [ctx]}]
  (let [endpoints (<!! (k8s/read-namespaced-endpoints ctx {:namespace "default" :name "atomix"}))
        hosts (->> endpoints :subsets first :addresses (map :ip))
        port (->> endpoints :subsets first :ports (filter #(= "netty" (:name %))) first :port)]
    (mapv #(Address. % port) hosts)))

(defrecord SeedComponent []
  IComponent
  (-start [{:keys [provider] :as self}]
    (log/info "-> Starting seed provider")
    (as-> self this
      (assoc this :ctx (case provider
                         :k8s (k8s/make-context "http://localhost:8001")
                         :default))
      (assoc this :cluster (get-nodes this))))
  (-stop [{:keys [storage] :as self}]
    (log/info "<- Stopping seed provider")
    (dissoc self :cluster :ctx)))
