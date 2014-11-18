(ns d3.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]
  )
