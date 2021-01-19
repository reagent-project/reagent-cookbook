(ns mojs-animation.core
    (:require [reagent.core :as reagent]
              [reagent.rdom :as rdom]))

(defn translate-y [node]
  (fn [progress]
    (set! (-> node .-style .-transform)
     (str "translateY(" (* 200 progress) "px)"))))

(defn animation-did-mount [this]
  (.run
   (js/mojs.Tween.
    (clj->js
     {:repeat 999
      :delay 2000
      :onUpdate (translate-y (rdom/dom-node this))}))))

(defn animation []
  (reagent/create-class {:render (fn [] [:div.square])
                         :component-did-mount animation-did-mount}))

(defn ^:export main []
  (rdom/render [animation]
                  (.getElementById js/document "app")))

