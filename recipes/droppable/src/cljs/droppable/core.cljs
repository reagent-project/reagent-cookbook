(ns droppable.core
    (:require [reagent.core :as reagent]))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#draggable.ui-widget-content [:p "Drag me to my target"]]
   [:div#droppable.ui-widget-header [:p "Drop here"]]
   ])

(defn home-did-mount []
  (js/$ (fn []
        (.draggable (js/$ "#draggable"))
        (.droppable (js/$ "#droppable")
                    #js {:drop (fn [event ui]
                                 (this-as this
                                          (.html (.find (.addClass (js/$ this) "ui-state-highlight") 
                                                        "p")
                                                 "Dropped!"))
                                 )}))))

(defn home-component []
  (reagent/create-class {:component-function home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
