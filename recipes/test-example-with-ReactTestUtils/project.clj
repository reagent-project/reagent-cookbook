(defproject test-example "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"]
                 [reagent "1.0.0":exclusions [cljsjs/react]]
                 [cljsjs/react-with-addons "15.6.1-0"]
                 [cljs-react-test "0.1.4-SNAPSHOT"]
                 [prismatic/dommy "1.1.0"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.8"]
            [lein-doo "0.1.11"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target" "test/js"]

  :cljsbuild {:builds [{:id "prod"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :pretty-print false}}

                       {:id "test"
                        :source-paths ["src/cljs" "test/cljs"]
                        :compiler {:output-to "resources/public/js/compiled/test.js"
                                   :main test-example.runner
                                   :optimizations :none}}
                       ]})
