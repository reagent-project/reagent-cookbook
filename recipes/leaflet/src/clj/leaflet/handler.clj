(ns leaflet.handler
    (:require [clojure.java.io :as io]
              [compojure.core :refer [GET defroutes]]
              [compojure.route :refer [not-found resources]]
              [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
              [prone.middleware :refer [wrap-exceptions]]))

(defroutes routes
  (GET "/" [] (slurp (io/resource "public/index.html")))
  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (wrap-defaults routes site-defaults)]
    (wrap-exceptions handler) handler))
