(ns droppable.core
    (:require
      [reagent.dom :as rdom]
      [reagent.core :as reagent]))

(def app-state (reagent/atom {:drop-area {:class "ui-widget-header"
                                          :text "Drop here"}}))

(defn draggable-render []
  [:div.ui-widget-content {:style {:width "100px"
                                   :height "100px" 
                                   :padding "0.5em"
                                   :float "left" 
                                   :margin "10px 10px 10px 0"}}
   [:p "Drag me to my target"]])

(defn draggable-did-mount [this]
  (.draggable (js/$ (rdom/dom-node this))))

(defn draggable []
  (reagent/create-class {:reagent-render draggable-render
                         :component-did-mount draggable-did-mount}))

(defn drop-area-render []
  (let [class (get-in @app-state [:drop-area :class])
        text (get-in @app-state [:drop-area :text])]
    [:div {:class class
           :style {:width "150px" 
                   :height "150px"
                   :padding "0.5em" 
                   :float "left" 
                   :margin "10px"}}
     [:p text]]))

(defn drop-area-did-mount [this]
  (.droppable (js/$ (rdom/dom-node this))
              #js {:drop (fn []
                           (swap! app-state assoc-in [:drop-area :class] "ui-widget-header ui-state-highlight")
                           (swap! app-state assoc-in [:drop-area :text] "Dropped!"))}))

(defn drop-area []
  (reagent/create-class {:reagent-render drop-area-render
                         :component-did-mount drop-area-did-mount}))

(defn home []
  [:div
   [draggable]
   [drop-area]])

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))

