(defproject deltaside "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:source-paths ["src" "tool-src"]}
             :uberjar {:aot :all}}
  :aliases {"brevity" ["run" "-m" "brevity.core/handle-commands" :project/main]}
  :main ^:skip-aot deltaside.core
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.immutant/web "2.1.10"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [ring/ring-json "0.5.0-beta1"]
                 [compojure "1.6.1"]
                 [buddy/buddy-auth "2.1.0"]
                 [buddy/buddy-sign "2.2.0"]
                 [buddy/buddy-hashers "1.3.0"]
                 [io.forward/yaml "1.0.7"]
                 [camel-snake-kebab "0.4.0"]
                 [org.clojure/spec.alpha "0.1.143"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [cheshire "5.8.0"]
                 [org.clojure/tools.logging "0.4.1"]

                 ;; cljs dependencies
                 [org.clojure/clojurescript "1.10.238"]
                 [secretary "1.2.3"]
                 [hiccup "2.0.0-alpha1"]
                 [garden "1.3.5"]
                 [reagent "0.8.0"]]
  :clean-targets ["static/development/js"
                  "static/release/js"
                  "static/development/index.js"
                  "static/development/index.js.map"
                  "out"
                  "target"]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["cljs-src"]
                :compiler
                {:output-to "static/development/index.js"
                 :source-map true
                 :output-dir "static/development/js"
                 :optimizations :none
                 :main deltaside.cljs.core
                 :asset-path "development/js"
                 :cache-analysis true
                 :pretty-print true}}
               {:id "release"
                :source-paths ["cljs-src"]
                :compiler
                {:output-to "static/release/index.js"
                 :source-map "static/release/index.js.map"
                 :externs []
                 :main deltaside.cljs.core
                 :output-dir "static/release/js"
                 :optimizations :advanced
                 :pseudo-names false}}]})
