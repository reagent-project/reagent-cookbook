(ns sticky-footer.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

  ;; Sticky Footer
  [:* {:margin "0"}]
  [:html :body {:height "100%"}]
  [:.page-wrap {:min-height "90%"
                ;; equal to footer height
                :margin-bottom "-100px"}]
  [:.page-wrap:after {:content "\"\""    ;notice the escape characters
                     :display "block"}]
  [:.site-footer :.page-wrap:after {:height "100px"}]
  [:.site-footer {:background "orange"}]
  )
