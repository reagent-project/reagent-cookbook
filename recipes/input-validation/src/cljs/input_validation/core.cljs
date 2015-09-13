(ns input-validation.core
    (:require [reagent.core :as reagent]))

(defn password-valid?
  "Valid if password is greater than 5 characters"
  [password]
  (> (count password) 5))

(defn password-color [password]
  (let [valid-color "green"
        invalid-color "red"]
    (if (password-valid? password) 
      valid-color 
      invalid-color)))

(def app-state (reagent/atom {:password nil}))

(defn password []
  [:input {:type "password"
           :on-change #(swap! app-state assoc :password (-> % .-target .-value))}])

(defn home []
  [:div {:style {:margin-top "30px"}}
   "Please enter a password greater than 5 characters. "
   [:span {:style {:padding "20px"
                   :background-color (password-color (@app-state :password))}}
    [password]
    ]])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

