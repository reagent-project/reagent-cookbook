(ns bootstrap-datepicker.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:input#example1 {:type "text" :placeholder "click to show datepicker"}]
   ])

(defn home-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"})))))

(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
