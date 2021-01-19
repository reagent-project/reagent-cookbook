(defproject mermaid "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"]
                 [ring "1.8.2"]
                 [ring/ring-defaults "0.3.2"]
                 [compojure "1.3.2"]
                 [prone "0.8.0"]
                 [reagent "1.0.0"]]

  :min-lein-version "2.5.0"

  :plugins [[lein-ring "0.12.5"]
            [lein-cljsbuild "1.1.8"]]

  :source-paths ["src/clj"]

  :ring {:handler mermaid.handler/app}

  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/app.js"}}]})
