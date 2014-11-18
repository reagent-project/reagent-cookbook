(ns google-maps.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#map-canvas]])

(defn home-did-mount []
  (let [map-canvas  (.getElementById js/document "map-canvas")
        map-options (clj->js {"center" (google.maps.LatLng. -34.397, 150.644)
                              "zoom"   8})]
    (js/google.maps.Map. map-canvas map-options)))

(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))