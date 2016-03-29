(ns filter-table.core
  (:require [reagent.core :as reagent]
            [clojure.string :as string]))

(def app-state
     (reagent/atom [{:id 1 :first-name "Jason" :last-name "Yates" :age "34"}
                    {:id 2 :first-name "Chris" :last-name "Wilson" :age "33"}
                    {:id 3 :first-name "John" :last-name "Lawrence" :age "32"}
                    {:id 4 :first-name "Albert" :last-name "Voxel" :age "67"}
                    {:id 5 :first-name "Zemby" :last-name "Alcoe" :age "495"}]))

(defn filter-content
  [filterstring]
  (filter #(re-find (->> (str filterstring)
                         (string/upper-case)
                         (re-pattern))
                    (string/upper-case (:first-name %)))
          @app-state))

(defn table
  [myfilter]
  [:table {:class "table table-condensed"}
   [:thead
    [:tr
     [:th "First Name"]
     [:th "Last Name"]
     [:th "Age"]]]
    [:tbody
     (for [{:keys [id
                   first-name
                   last-name
                   age]} (filter-content myfilter)]
       ^{:key id}
       [:tr
         [:td first-name]
         [:td last-name]
         [:td age]])]])

(defn search-table
  []
  (let [filter-value (reagent/atom nil)]
    (fn []
      [:div
       [:input {:type      "text" :value @filter-value
                :on-change #(reset! filter-value (-> % .-target .-value))}]
       [table @filter-value]])))

(defn home []
  [:div.container
   [search-table]])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))
