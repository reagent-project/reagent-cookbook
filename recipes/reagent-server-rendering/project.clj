(defproject reagent-server-rendering "0.1.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"]
                 [reagent "1.0.0"]
                 [ring "1.4.0"] ;
                 [ring/ring-defaults "0.1.5"] ;
                 [hiccup "1.0.5"] ;
                 [aleph "0.4.0"]] ;;

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.8"]
            [lein-ring "0.9.6"]] ;

  :ring {:handler reagent-server-rendering.handler/app}

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target" "test/js"]

  :cljsbuild {:builds [{:id "prod"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :pretty-print false}}]})
