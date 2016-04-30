(ns atomix-playground.transport
  (:require
   [hara.component :refer [IComponent]]
   [taoensso.timbre :refer [refer-timbre]])
  (import
   [io.atomix.catalyst.transport NettyTransport]))

(refer-timbre)

(defrecord TransportComponent []
  IComponent
  (-start [self]
    (info "-> Starting transport")
    (assoc self :transport (NettyTransport.)))
  (-stop [{:keys [transport] :as self}]
    (info "<- Stopping transport")
    (.close transport)
    (dissoc self :transport)))
