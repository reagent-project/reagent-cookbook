(ns google-maps.core
    (:require [reagent.core :as reagent]))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#map-canvas]   
   ])

(defn home-did-mount []
  (let [map-canvas  (.getElementById js/document "map-canvas")
        map-options (clj->js {"center" (google.maps.LatLng. -34.397, 150.644)
                              "zoom" 8})]
        (js/google.maps.Map. map-canvas map-options)))

(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
