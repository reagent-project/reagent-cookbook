(ns test-example.core
    (:require [reagent.dom :as rdom]))

(defn home []
  [:div

   ])

(defn ^:export main []
  (rdom/render [home]
                  (.getElementById js/document "app")))
