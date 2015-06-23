(defproject markdown-editor "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3211"]
                 [reagent "0.5.0"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.3"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :figwheel     {:on-jsload "markdown-editor.core/main"}
                        :compiler     {;:optimizations :advanced
                                       :main       markdown-editor.core
                                       :output-to  "resources/public/js/compiled/app.js"
                                       :output-dir "resources/public/js/compiled/out"
                                       :asset-path "js/compiled/out"
                                       :externs    ["externs/syntax.js"]}}]})
