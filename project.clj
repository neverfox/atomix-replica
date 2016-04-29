(defproject atomix-playground "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[environ "1.0.2"]
                 [im.chit/hara.component "2.2.17"]
                 [io.atomix/atomix-all "1.0.0-rc4"]
                 [org.clojure/clojure "1.8.0"]]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
                   :source-paths ["env/dev" "src"]}})
