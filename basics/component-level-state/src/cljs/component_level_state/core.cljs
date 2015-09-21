(ns component-level-state.core
    (:require [reagent.core :as reagent]))

;; Do this
(defn foo []  ;; A function
  (let [component-state (reagent/atom {:count 0})] ;; <-- not included in `render`
    (fn []  ;; That returns a function  <-- `render` is from here down
      [:div ;; That returns hiccup
       [:p "Current count is: " (get @component-state :count)]
       [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]])))

;; Don't do this
(defn foo-mistake []
  (let [component-state (reagent/atom {:count 0})]
    [:div
     [:p "Current count is: " (get @component-state :count)]
     [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))

;; Don't do this
(defn foo-mistake2 []
  (let [component-state (reagent/atom {:count 0})]
    [:div
     [:p "Current count is: " (get @component-state :count)] ;; <-- This deref is causing the re-render
     (.log js/console (str "Foo Mistake 2 is being rendered")) ;; <- will print this on-click
     [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))

;; Do this
(defn foo-inner-let []
  (let [component-state (reagent/atom {:count 0})]
    (fn [] ;; <-- `render` is from here down
      (let [count (get @component-state :count)] ;; let block is inside `render`
        [:div
         [:p "Current count is: " count]
         [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))))

(defn home []
  [:div
   [:h1 "Foo"]
   [foo]
   [:h1 "Foo Mistake"]
   [foo-mistake]
   [:h1 "Foo Mistake 2"]
   [foo-mistake2]
   [:h1 "Foo Inner Let"]
   [foo-inner-let]
   ])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

