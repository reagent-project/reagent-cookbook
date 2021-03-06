(ns google-maps.core
    (:require
      [reagent.dom :as rdom]
      [reagent.core :as reagent]))

(defn home-render []
  [:div {:style {:height "300px"}}
   ])

(defn home-did-mount [this]
  (let [map-canvas (rdom/dom-node this)
        map-options (clj->js {"center" (js/google.maps.LatLng. -34.397, 150.644)
                              "zoom" 8})]
    (js/google.maps.Map. map-canvas map-options)))

(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))
