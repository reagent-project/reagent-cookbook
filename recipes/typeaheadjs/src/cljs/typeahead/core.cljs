(ns typeahead.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [clojure.string :as s]))

(def states
  ["Alabama" "Alaska" "Arizona" "Arkansas" "California"
   "Colorado" "Connecticut" "Delaware" "Florida" "Georgia" "Hawaii"
   "Idaho" "Illinois" "Indiana" "Iowa" "Kansas" "Kentucky" "Louisiana"
   "Maine" "Maryland" "Massachusetts" "Michigan" "Minnesota"
   "Mississippi" "Missouri" "Montana" "Nebraska" "Nevada" "New Hampshire"
   "New Jersey" "New Mexico" "New York" "North Carolina" "North Dakota"
   "Ohio" "Oklahoma" "Oregon" "Pennsylvania" "Rhode Island"
   "South Carolina" "South Dakota" "Tennessee" "Texas" "Utah" "Vermont"
   "Virginia" "Washington" "West Virginia" "Wisconsin" "Wyoming"])

(defn matcher [strs]
  (fn [text callback]
    (->> strs
         (filter #(s/includes? % text))
         (clj->js)
         (callback))))

(defn typeahead-mounted [this]
  (.typeahead (js/$ (rdom/dom-node this))
              (clj->js {:hint true
                        :highlight true
                        :minLength 1})
              (clj->js {:name "states"
                        :source (matcher states)})))

(def typeahead-value (reagent/atom nil))

(defn render-typeahead []
  [:input.typeahead
   {:type :text
    :on-select #(reset! typeahead-value (-> % .-target .-value))
    :placeholder "States of USA"}])

(defn typeahead []
  (reagent/create-class
    {:component-did-mount typeahead-mounted
     :reagent-render render-typeahead}))

(defn home []
  [:div.ui-widget
   [:p "selected state: " @typeahead-value]
   [typeahead]])

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))


