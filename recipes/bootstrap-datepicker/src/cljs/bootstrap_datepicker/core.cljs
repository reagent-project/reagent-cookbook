(ns bootstrap-datepicker.core
    (:require [reagent.core :as reagent]))

(defn home-render []
  [:input {:type "text" :placeholder "click to show datepicker"}])

(defn home-did-mount [this]
  (.datepicker (js/$ (reagent/dom-node this)) (clj->js {:format "dd/mm/yyyy"})))

(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))
