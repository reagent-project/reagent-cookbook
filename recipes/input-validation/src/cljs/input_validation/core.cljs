(ns input-validation.core
  (:require [reagent.core :as reagent]))

(defn input-valid?
  "Valid if input is less than 10 characters"
  [x]
  (> 10 (count x)))

(defn color [input]
  (let [valid-color "green"
        invalid-color "red"]
    (if (input-valid? input) 
      valid-color invalid-color)))

(defn home []
  (let [state (reagent/atom {:user-input "some value"})]
    (fn []
      [:div [:h1 "Welcome to Reagent Cookbook!"]
       [:span {:style {:padding "20px"
                       :background-color (color (@state :user-input))}}
        [:input {:value (@state :user-input)
                 :on-change #(swap! state assoc :user-input (-> % .-target .-value))
                 }]]])))

(reagent/render-component [home]
                          (.getElementById js/document "app"))
