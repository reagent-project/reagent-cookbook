(ns highcharts.core
    (:require [reagent.core :as reagent]))

(def chart-config
  {:chart {:type "bar"}
   :title {:text "Historic World Population by Region"}
   :subtitle {:text "Source: Wikipedia.org"}
   :xAxis {:categories ["Africa" "America" "Asia" "Europe" "Oceania"]
           :title {:text nil}}
   :yAxis {:min 0
           :title {:text "Population (millions)"
                   :align "high"}
           :labels {:overflow "justify"}}
   :tooltip {:valueSuffix " millions"}
   :plotOptions {:bar {:dataLabels {:enabled true}}}
   :legend {:layout "vertical"
            :align "right"
            :verticalAlign "top"
            :x -40
            :y 100
            :floating true
            :borderWidth 1
            :shadow true}
   :credits {:enabled false}
   :series [{:name "Year 1800"
             :data [107 31 635 203 2]}
            {:name "Year 1900"
             :data [133 156 947 408 6]}
            {:name "Year 2008"
             :data [973 914 4054 732 34]}]
   })

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#example {:style {:min-width "310px" :max-width "800px" 
                          :height "400px" :margin "0 auto"}}]
   ])

(defn home-did-mount []
  (js/$ (fn []
          (.highcharts (js/$ "#example")
                       (clj->js chart-config)))))

(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
