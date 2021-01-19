(ns draggable.core
    (:require [reagent.dom :as rdom]
              [reagent.core :as reagent]))

(defn home-render []
  [:div.ui-widget-content {:style {:width "150px" 
                                   :height "150px" 
                                   :padding "0.5em"}}
   [:p "Drag me around"]])

(defn home-did-mount [this]
  (.draggable (js/$ (rdom/dom-node this))))

(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))

