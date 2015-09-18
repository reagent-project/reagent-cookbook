(ns basic-component.core
  (:require [reagent.core :as reagent]))

;; Form-3 Component
(defn foo []
  (reagent/create-class {:reagent-render (fn [] [:div "Hello, world!"])}))

;; Form-1 Component
(defn bar []
  [:div "Hello, world!"])

;; Form-2 Component
(defn baz []
  (fn []
    [:div "Hello, world!"]))

(defn home []
  [:div
   [foo]
   [bar]
   [baz]
   ])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

