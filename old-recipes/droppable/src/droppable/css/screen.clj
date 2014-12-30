(ns droppable.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

  [:#draggable {:width "100px" :height "100px" :padding (em 0.5) :float "left" :margin "10px 10px 10px 0"}]
  [:#droppable {:width "150px" :height "150px" :padding (em 0.5) :float "left" :margin "10px"}]
  [:#total-drops {:font-size (em 1.5) :color "red"}]
  )
