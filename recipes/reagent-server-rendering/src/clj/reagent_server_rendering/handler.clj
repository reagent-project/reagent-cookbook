(ns reagent-server-rendering.handler
  (:require [aleph.flow :as flow]
            [clojure.java.io :as io]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]])
  (:import [io.aleph.dirigiste Pools]
           [javax.script ScriptEngineManager Invocable]))

(defn- create-js-engine []
  (doto (.getEngineByName (ScriptEngineManager.) "nashorn")
    ; React requires either "window" or "global" to be defined.
    (.eval "var global = this")
    (.eval (-> "public/js/compiled/app.js"
               io/resource
               io/reader))))

; We have one and only one key in the pool, it's a constant.
(def ^:private js-engine-key "js-engine")
(def ^:private js-engine-pool
  (flow/instrumented-pool
    {:generate   (fn [_] (create-js-engine))
     :controller (Pools/utilizationController 0.9 10000 10000)}))

(defn- render-page [page-id]
  (let [js-engine @(flow/acquire js-engine-pool js-engine-key)]
    (try (.invokeMethod
           ^Invocable js-engine
           (.eval js-engine "reagent_server_rendering.core")
           "render_page" (object-array [page-id]))
         (finally (flow/release js-engine-pool js-engine-key js-engine)))))

(defn page [page-id]
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css "css/site.css")]
    [:body
     [:div#app
      (render-page page-id)]
     (include-js "js/compiled/app.js")
     [:script {:type "text/javascript"}
      (str "main('" page-id "');")]]]))

(defroutes app-routes
  (GET "/" [] (page "home"))
  (GET "/about" [] (page "about"))
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-defaults app-routes site-defaults))
