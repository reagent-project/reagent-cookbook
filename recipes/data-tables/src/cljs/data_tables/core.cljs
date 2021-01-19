(ns data-tables.core
  (:require
   [reagent.dom :as rdom]
   [reagent.core :as reagent]))

(defn home-render []
  [:table.table.table-striped.table-bordered 
   {:cell-spacing "0" :width "100%"}

   [:thead>tr 
    [:th "Name"]
    [:th "Age"]]

   [:tbody
    [:tr 
     [:td "Matthew"]
     [:td "26"]]

    [:tr 
     [:td "Anna"]
     [:td "24"]]
    
    [:tr 
     [:td "Michelle"]
     [:td "42"]]
    
    [:tr 
     [:td "Frank"]
     [:td "46"]]
    ]])

(defn home-did-mount [this]
  (.DataTable (js/$ (reagent/dom-node this))))


(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (rdom/render [home]
                  (.getElementById js/document "app")))

