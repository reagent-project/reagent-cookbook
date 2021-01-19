(ns test-example.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as rdom]))

(defonce app-state (reagent/atom {:count 0}))

(defn handle-on-click [app-state]
  (swap! app-state update-in [:count] inc))

(defn increment-button [app-state]
  [:button {:on-click #(handle-on-click app-state)}
   "Increment"])

(defn home []
  [:div
   [increment-button app-state]
   [:div "Current count: " (@app-state :count)]
   ])

(defn ^:export main []
  (rdom/render [home]
                  (.getElementById js/document "app")))
