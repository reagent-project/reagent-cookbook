(ns toggle-class.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as rdom]))

(defn toggle-class [a k class1 class2]
  (if (= (@a k) class1)
    (swap! a assoc k class2)
    (swap! a assoc k class1)))

(defn home []
  (let [local-state (reagent/atom {:btn-class "btn btn-default"})]
    (fn []
      [:div {:class (@local-state :btn-class)
             :on-click #(toggle-class local-state :btn-class "btn btn-default" "btn btn-danger")}
       "Click me"])))

(defn ^:export main []
  (rdom/render [home]
                  (.getElementById js/document "app")))

