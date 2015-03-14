(ns data-tables.core
  (:require [reagent.core :as reagent]))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div.row
    [:div.col-md-6
     [:table#example.table.table-striped.table-bordered {:cell-spacing "0" :width "100%"}
      [:thead
       [:tr [:th "Name"]
        [:th "Age"]]]
      [:tbody
       [:tr [:td "Matthew"]
        [:td "26"]]
       [:tr [:td "Anna"]
        [:td "24"]]
       [:tr [:td "Michelle"]
        [:td "42"]]
       [:tr [:td "Frank"]
        [:td "46"]]]]]]
   ])

(defn home-did-mount []
  (.ready (js/$ js/document) (fn []
                               (.DataTable (js/$ "#example"))
                               )))

(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
