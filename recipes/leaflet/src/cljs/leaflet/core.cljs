(ns leaflet.core
    (:require [reagent.dom :as rdom]
              [reagent.core :as reagent]))

(defn home-render []
  [:div#map {:style {:height "360px"}}])

(defn home-did-mount []
  (let [map (.setView (.map js/L "map") #js [51.505 -0.09] 13)]

    ;; NEED TO UPDATE with your mapID
    (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/thedon73v.k3goc602/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18}))
            map)))

(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))

