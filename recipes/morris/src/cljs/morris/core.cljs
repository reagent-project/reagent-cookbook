(ns morris.core
    (:require [reagent.core :as reagent]))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#donut-example ]
   ])

(defn home-did-mount []
  (.Donut js/Morris (clj->js {:element "donut-example"
                              :data [{:label "Download Sales" :value 12}
                                     {:label "In-Store Sales" :value 30}
                                     {:label "Mail-Order Sales" :value 20}]})))

(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
