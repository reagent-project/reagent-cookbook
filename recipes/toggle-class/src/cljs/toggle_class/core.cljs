(ns toggle-class.core
    (:require [reagent.core :as reagent]))

(defn toggle-class [a k class1 class2]
  (if (= (@a k) class1)
    (swap! a assoc k class2)
    (swap! a assoc k class1)))

(defn home []
  (let [state (reagent/atom {:btn-class "btn btn-default"})]
    (fn []
      [:div [:h1 "Welcome to Reagent Cookbook!"]
       [:div {:class (@state :btn-class)
              :on-click #(toggle-class state :btn-class "btn btn-default" "btn btn-danger")}
        "Click me"]
       ])))

(reagent/render-component [home]
                          (.getElementById js/document "app"))
