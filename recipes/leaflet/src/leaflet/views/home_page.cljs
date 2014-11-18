(ns leaflet.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#map ]
   ])

(defn home-did-mount []
  (let [map (.setView (.map js/L "map") #js [51.505 -0.09] 13)]
    
    ;;; NEED TO REPLACE FIXME with your mapID!
    (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/FIXME/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18}))
            map)))

(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
