(ns local-storage.core
    (:require [reagent.core :as reagent]
              [alandipert.storage-atom :refer [local-storage]]))

(def app-state (local-storage 
                (reagent/atom {:counter 0})
                :app-state))

(defn home []
  [:div
   [:div "Current count: " (@app-state :counter)]
   [:button {:on-click #(swap! app-state update-in [:counter] inc)} 
    "Increment"]])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

