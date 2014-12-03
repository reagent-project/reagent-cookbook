(ns data-from-ajax.handler
  (:require [data-from-ajax.dev :refer [browser-repl start-figwheel]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [selmer.parser :refer [render-file]]
            [environ.core :refer [env]]
            [prone.middleware :refer [wrap-exceptions]]
            [cognitect.transit :as transit]
            [ring.util.response :refer [content-type response]])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(def data {"sample-data"
           {:title "This is sample data"
            :content "Some interesting information about something"}})

(defn encode-transit [data]
  (let [out (ByteArrayOutputStream.)]
    (transit/write (transit/writer out :json) data)
    (String. (.toByteArray out))))

(defroutes routes
  (GET "/" [] (render-file "templates/index.html" {:dev (env :dev?)}))
  (GET "/data/:id" [id]
       (-> (get data id)
           (encode-transit)
           (response)
           (content-type "application/transit+json")))
  (resources "/")
  (not-found "Not Found"))

(def app
  (if (env :dev?) (wrap-exceptions routes) routes))
