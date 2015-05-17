(ns filter-and-sort-table.core
    (:require [reagent.core :as reagent]
              [reagent.core :as reagent :refer [atom]]))

(def select-value (atom "first-name"))
(def filter-value (atom ""))

(def table-contents
  [{:id 1 :first-name "Bram"    :last-name "Moolenaar"  :known-for "Vim"}
   {:id 2 :first-name "Richard" :last-name "Stallman"   :known-for "GNU"}
   {:id 3 :first-name "Dennis"  :last-name "Ritchie"    :known-for "C"}
   {:id 4 :first-name "Rich"    :last-name "Hickey"     :known-for "Clojure"}
   {:id 5 :first-name "Guido"   :last-name "Van Rossum" :known-for "Python"}
   {:id 6 :first-name "Linus"   :last-name "Torvalds"   :known-for "Linux"}
   {:id 7 :first-name "Yehuda"  :last-name "Katz"       :known-for "Ember"}])

(defn table-select-box [value]
  [:select {:value     @value
            :on-change #(reset! value (-> % .-target .-value))}
   [:option {:value "first-name"} "First Name"]
   [:option {:value "last-name"}  "Last Name"]
   [:option {:value "known-for"}  "Known For"]])

(defn table-filter-box [filter-val]
  [:input {:type        "text"
           :value       @filter-val
           :placeholder "Filter contents"
           :on-change   #(reset! filter-val (-> % .-target .-value))}])

(defn filtered-table-contents [contents filter-val]
  (if (empty? filter-val)
    contents
    (filter #(or (.startsWith (:first-name %) filter-val)
                 (.startsWith (:last-name  %) filter-val)
                 (.startsWith (:known-for  %) filter-val)) contents)))

(defn filtered-and-sorted-contents [select-val filter-val contents]
  (sort-by (keyword @select-val)
    (filtered-table-contents contents @filter-val)))

(defn table [select-val filter-val contents]
  [:table
    [:thead
      [:tr [:th {:width "200"} "First Name"] [:th {:width "200"} "Last Name"] [:th {:width "200"} "Known For"]]]
    [:tbody
      (for [person (filtered-and-sorted-contents select-val filter-val contents)]
        ^{:key (:id person)} [:tr [:td (:first-name person)] [:td (:last-name person)] [:td (:known-for person)]])]])

(defn table-page []
  [:div {:id "table-wrap"}
    [table-select-box select-value]
    [table-filter-box filter-value]
    [table select-value filter-value table-contents]])

(reagent/render-component [table-page]
                          (.getElementById js/document "app"))
