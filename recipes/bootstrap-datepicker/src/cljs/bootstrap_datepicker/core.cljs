(ns bootstrap-datepicker.core
    (:require [reagent.core :as reagent]))

(defn datepicker-render []
  [:input#example1 {:type "text" :placeholder "click to show datepicker"}])

(defn datepicker-did-mount [this]
  (.datepicker (js/$ (reagent/dom-node this)) (clj->js {:format "dd/mm/yyyy"})))

(defn datepicker []
  (reagent/create-class {:reagent-render datepicker-render
                         :component-did-mount datepicker-did-mount}))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [datepicker]])

(reagent/render [home] (.getElementById js/document "app"))
