(ns leaflet.core
    (:require [reagent.core :as reagent]))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#map ]
   ])

(defn home-did-mount []
  (let [map (.setView (.map js/L "map") #js [51.505 -0.09] 13)]
    ;; NEED TO REPLACE FIXME with your mapID!
    (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/FIXME/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18}))
            map)))

(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
