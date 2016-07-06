(defproject atomix-replica "0.1.0"
  :description "Atomix replica as a Clojure system"
  :url "https://github.com/neverfox/atomix-replica"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[aero "1.0.0-beta5"]
                 [beckon "0.1.1"]
                 [ch.qos.logback/logback-classic "1.1.7"]
                 [ch.qos.logback/logback-core "1.1.7"]
                 [im.chit/hara.component "2.3.7"]
                 [io.atomix/atomix-all "1.0.0-rc9"]
                 [kubernetes-api "0.1.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.385"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-api "1.7.21"]]
  :main ^:skip-aot atomix-replica.core
  :target-path "target/%s"
  :repl-options {:init-ns user}
  :profiles {:uberjar {:aot          :all
                       :uberjar-name "replica.jar"}
             :dev     {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
                       :source-paths ["env/dev" "src"]}})
