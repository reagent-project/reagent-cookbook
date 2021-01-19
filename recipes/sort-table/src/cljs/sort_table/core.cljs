(ns sort-table.core
    (:require [reagent.core :as reagent]
              [reagent.dom :as dom]))

(def app-state (reagent/atom {:sort-val :first-name :ascending true}))

(def table-contents
  [{:id 1 :first-name "Bram"    :last-name "Moolenaar"  :known-for "Vim"}
   {:id 2 :first-name "Richard" :last-name "Stallman"   :known-for "GNU"}
   {:id 3 :first-name "Dennis"  :last-name "Ritchie"    :known-for "C"}
   {:id 4 :first-name "Rich"    :last-name "Hickey"     :known-for "Clojure"}
   {:id 5 :first-name "Guido"   :last-name "Van Rossum" :known-for "Python"}
   {:id 6 :first-name "Linus"   :last-name "Torvalds"   :known-for "Linux"}
   {:id 7 :first-name "Yehuda"  :last-name "Katz"       :known-for "Ember"}])

(defn update-sort-value [new-val]
  (if (= new-val (:sort-val @app-state))
    (swap! app-state update-in [:ascending] not)
    (swap! app-state assoc :ascending true))
  (swap! app-state assoc :sort-val new-val))

(defn sorted-contents []
  (let [sorted-contents (sort-by (:sort-val @app-state) table-contents)]
    (if (:ascending @app-state)
      sorted-contents
      (rseq sorted-contents))))

(defn table []
  [:table
    [:thead
      [:tr
        [:th {:width "200" :on-click #(update-sort-value :first-name)} "First Name"]
        [:th {:width "200" :on-click #(update-sort-value :last-name) } "Last Name"]
        [:th {:width "200" :on-click #(update-sort-value :known-for) } "Known For"]]]
    [:tbody
      (for [person (sorted-contents)]
        ^{:key (:id person)} 
        [:tr [:td (:first-name person)] 
         [:td (:last-name person)] 
         [:td (:known-for person)]])]])

(defn home []
  [:div {:style {:margin "auto"
                 :padding-top "30px"
                 :width "600px"}}
    [table]])

(defn ^:export main []
  (rdom/render [home]
                  (.getElementById js/document "app")))

