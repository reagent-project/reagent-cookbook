(ns draggable.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

  ;; draggable element
  [:#draggable {:width "150px"
                :height "150px"
                :padding (em 0.5)}]
  )
