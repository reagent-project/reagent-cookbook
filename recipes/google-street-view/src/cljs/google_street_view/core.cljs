(ns google-street-view.core
    (:require
      [reagent.dom :as rdom]
      [reagent.core :as reagent]))

(def base-url 
  "http://maps.googleapis.com/maps/api/streetview?size=600x400&location=")

(defn address-url [street city]
  (str street ", " city))

(defn street-view-url [street city]
  (str base-url 
       (address-url street city)))

(def app-state (reagent/atom {:street "24 Willie Mays Plaza" :city "San Francisco"}))

(defn input [k]
  [:input {:value (@app-state k)
           :on-change #(swap! app-state assoc k (-> % .-target .-value))}])

(defn home []
  [:div
   [:p "Street: " [input :street]]
   [:p "City: " [input :city]] 
   [:img {:src (street-view-url (@app-state :street) (@app-state :city))}]])

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))

