(ns user
  (:require [cemerick.austin.repls :refer (browser-connected-repl-js)]
            [net.cgrand.enlive-html :as enlive]
            [compojure.route :refer (resources)]
            [compojure.core :refer (GET defroutes)]
            [ring.adapter.jetty]
            [clojure.java.io :as io]))

(defonce repl-env (reset! cemerick.austin.repls/browser-repl-env
                          (cemerick.austin/repl-env)))

(defn page
  "In order to have a REPL connected to our app, we need to add a script tag
   that includes the client-side part of the REPL that talks to the REPL server."
  [file-name]
  (enlive/sniptest (slurp (io/resource (str file-name ".html")))
                   [:body] (enlive/append
                            (enlive/html [:script (browser-connected-repl-js)]))))

;; Serve all files from resources/public as is. Serve the index page with the
;; REPL connected. In order to connect a REPL to any page, you must make sure the
;; script is added to all HTML files.
(defroutes site
  (resources "/")
  (GET "/*" req (page "public/index")))

(defn run!
  "Call this function to run a server on port 8080"
  []
  (defonce ^:private server
    (ring.adapter.jetty/run-jetty #'site {:port 8080 :join? false}))
  server)

(defn cljs!
  "Once you have started your Clojure REPL, enter the user namespace and run
   this function."
  []
  (cemerick.austin.repls/cljs-repl repl-env))
