(defproject autocomplete "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [reagent "0.6.0-rc"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.4-7"]]

  :clean-targets ^{:protect false} ["resources/public/js" "target"]

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "autocomplete.core/reload"}
     :compiler     {:main                 autocomplete.core
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/out"
                    :asset-path           "js/out"
                    :source-map-timestamp true}}

    {:id           "prod"
     :source-paths ["src/cljs"]
     :compiler     {:output-to     "resources/public/js/app.js"
                    :optimizations :advanced
                    :pretty-print  false}}]})
