(ns morris.core
    (:require [reagent.core :as reagent]))

(defn home-render []
  [:div#donut-example ])

(defn home-did-mount []
  (.Donut js/Morris (clj->js {:element "donut-example"
                              :data [{:label "Download Sales" :value 12}
                                     {:label "In-Store Sales" :value 30}
                                     {:label "Mail-Order Sales" :value 20}]})))

(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

