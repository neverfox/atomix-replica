(ns atomix-playground.storage
  (:require
   [hara.component :refer [IComponent]]
   [taoensso.timbre :refer [refer-timbre]])
  (import
   [io.atomix.copycat.server.storage Storage StorageCleaner StorageLevel]
   [java.util UUID]))

(refer-timbre)

(defmulti build-storage :level :default :disk)

(defmethod build-storage :disk
  [config]
  (-> (Storage/builder)
      (.withDirectory (str (get config :path (str (System/getProperty "user.dir") "/logs/" (UUID/randomUUID)))))
      (.build)))

(defmethod build-storage :memory
  [_]
  (Storage. StorageLevel/MEMORY))

(defrecord StorageComponent []
  IComponent
  (-start [self]
    (info "-> Starting storage")
    (assoc self :storage (build-storage self)))
  (-stop [{:keys [storage] :as self}]
    (info "<- Stopping storage")
    (.. (StorageCleaner. storage)
        (cleanFiles (reify java.util.function.Predicate
                      (test [this f] true))))
    (dissoc self :storage)))
