(ns sortable-portlets.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

  ;; sortable portlets
  [:body {:min-width "520px"}]
  [:.column {:width "170px"
             :float "left"
             :padding-bottom "100px"}]
  [:.portlet {:margin "0 1em 1em 0"
              :padding "0.3em"}]
  [:.portlet-header {:padding "0.2em 0.3em"
                     :margin-bottom "0.5em"
                     :position "relative"}]
  [:.portlet-toggle {:position "absolute"
                     :top "50%"
                     :right "0"
                     :margin-top "-8px"}]
  [:.portlet-content {:padding "0.4em"}]
  [:.portlet-placeholder {:border "1px dotted black"
                          :margin "0 1em 1em 0"
                          :height "50px"}]
  )
