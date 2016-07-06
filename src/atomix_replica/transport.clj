(ns atomix-replica.transport
  (:require
   [clojure.tools.logging :as log]
   [hara.component :refer [IComponent]])
  (import
   [io.atomix.catalyst.transport.netty NettyTransport]))

(defrecord TransportComponent []
  IComponent
  (-start [self]
    (log/info "-> Starting transport")
    (assoc self :transport (NettyTransport.)))
  (-stop [{:keys [transport] :as self}]
    (log/info "<- Stopping transport")
    (.close transport)
    (dissoc self :transport)))
