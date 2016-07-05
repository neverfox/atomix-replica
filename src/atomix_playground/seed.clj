(ns atomix-playground.seed
  (:require
   [clojure.core.async :refer [<!!]]
   [environ.core :refer [env]]
   [hara.component :refer [IComponent]]
   [kubernetes.api.v1 :as k8s]
   [taoensso.timbre :refer [refer-timbre]])
  (import
   [io.atomix.catalyst.transport Address]))

(refer-timbre)

(defn make-cluster [coll]
  (mapv #(Address. %) coll))

(defn string->cluster [s]
  (-> s
      (#(clojure.string/split % #","))
      make-cluster))

(defmulti get-nodes :provider :default :none)

(defmethod get-nodes :none [_] nil)

(defmethod get-nodes :local [{:keys [nodes]}] (make-cluster nodes))

(defmethod get-nodes :env [_] (string->cluster (env :nodes)))

(defmethod get-nodes :k8s
  [{:keys [ctx]}]
  (let [endpoints (k8s/read-namespaced-endpoints ctx {:namespace "default" :name "atomix"})
        hosts (->> endpoints :subsets first :addresses (map :ip))
        port (->> endpoints :ports (filter (= :name "netty")) first :port)]
    (mapv #(Address. % port) hosts)))

(defrecord SeedComponent []
  IComponent
  (-start [{:keys [provider] :as self}]
    (info "-> Starting seed provider")
    (case provider
      :k8s (assoc self :ctx (k8s/make-context "http://localhost:8001"))
      :default)
    (let [cluster (get-nodes self)]
      (info "Seeding with cluster" cluster)
      (assoc self :cluster cluster)))
  (-stop [{:keys [storage] :as self}]
    (info "<- Stopping seed provider")
    (dissoc self :cluster :ctx)))