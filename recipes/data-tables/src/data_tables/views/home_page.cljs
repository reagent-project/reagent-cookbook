(ns data-tables.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:table#example.table.table-striped.table-bordered {:cell-spacing "0" :width "100%"}
    [:thead
     [:tr [:th "Name"]
      [:th "Age"]]]
    [:tbody
     [:tr [:td "Matthew"]
      [:td "26"]]
     [:tr [:td "Anna"]
      [:td "14"]]
     [:tr [:td "Michelle"]
      [:td "42"]]
     [:tr [:td "Frank"]
      [:td "37"]]]]
   ])

(defn home-did-mount []
  (.ready (js/$ js/document) (fn []
                               (.DataTable (js/$ "#example"))
                               )))

(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
