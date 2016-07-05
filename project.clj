(defproject atomix-playground "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[beckon "0.1.1"]
                 [environ "1.0.3"]
                 [ch.qos.logback/logback-classic "1.1.7"]
                 [ch.qos.logback/logback-core "1.1.7"]
                 [com.fzakaria/slf4j-timbre "0.3.2"]
                 [com.taoensso/timbre "4.3.1"]
                 [im.chit/hara.component "2.3.6"]
                 [io.atomix/atomix-all "1.0.0-rc9"]
                 [kubernetes-api "0.1.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.374"]
                 [org.slf4j/slf4j-api "1.7.21"]]
  :main ^:skip-aot atomix-playground.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :uberjar-name "atomix-playground-standalone.jar"}
             :dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
                   :source-paths ["env/dev" "src"]}})
