(ns compare-argv.core
    (:require [reagent.core :as reagent]))

(defn home []
  [:div
   
   ])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

