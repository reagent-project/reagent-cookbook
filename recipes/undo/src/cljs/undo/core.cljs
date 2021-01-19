(ns undo.core
    (:require [reagent.core :as reagent]
              [reagent.dom :as rdom]
              [historian.core :as hist]))

(def app-state (reagent/atom {:counter 0}))

(hist/record! app-state :app-state)

(defn home []
  [:div
   [:div "Current count: " (@app-state :counter)]
   [:button {:on-click #(swap! app-state update-in [:counter] inc)} "Increment"]
   [:button {:on-click #(hist/undo!)} "Undo"] ])

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))

