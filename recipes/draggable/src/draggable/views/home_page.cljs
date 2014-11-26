(ns draggable.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#draggable.ui-widget-content [:p "Drag me around"]]
   ])

(defn home-did-mount []
  (js/$ (fn []
          (.draggable (js/$ "#draggable"))
          )))

(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
