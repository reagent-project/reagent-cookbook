(defproject draggable "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3058" :scope "provided"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.3"]
                 [compojure "1.3.2"]
                 [prone "0.8.0"]
                 [reagent "0.5.0"]]

  :min-lein-version "2.5.0"

  :plugins [[lein-ring "0.9.1"]
            [lein-cljsbuild "1.0.4"]]

  :source-paths ["src/clj"]

  :ring {:handler draggable.handler/app}

  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/app.js"}}]})
