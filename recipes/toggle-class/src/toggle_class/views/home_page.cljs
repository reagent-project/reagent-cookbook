(ns toggle-class.views.home-page
  (:require [reagent.core :as reagent :refer [atom]]))

(defn toggle-class [a k class1 class2]
  (if (= (get @a k) class1)
    (swap! a assoc k class2)
    (swap! a assoc k class1)))

(defn home-page []
  (let [state (atom {:btn-class "btn btn-default"})]  ; now this atom is a reagent atom!
    (fn []
      [:div
       [:h2 "Home Page"]
       [:div "Woot! You are starting a reagent application."]

       [:div {:class (@state :btn-class)
              :on-click (fn [] (toggle-class state :btn-class "btn btn-default" "btn btn-danger"))
              } "Click me"]])))
