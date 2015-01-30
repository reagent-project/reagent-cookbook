(defproject mermaid "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2496"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.2"]
                 [compojure "1.3.1"]
                 [prone "0.8.0"]
                 [reagent "0.4.3"]]

  :min-lein-version "2.5.0"

  :plugins [[lein-ring "0.8.13"]
            [lein-cljsbuild "1.0.3"]]

  :source-paths ["src/clj"]

  :ring {:handler mermaid.handler/app}

  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/app.js"}}]})
