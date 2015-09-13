(ns nvd3.core
  (:require [reagent.core :as reagent]))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#d3-node {:style {:width "750" :height "420"}} [:svg ]]
   ])

(defn home-did-mount []
  (.addGraph js/nv (fn []
                     (let [chart (.. js/nv -models lineChart
                                     (margin #js {:left 100})
                                     (useInteractiveGuideline true)
                                     (transitionDuration 350)
                                     (showLegend true)
                                     (showYAxis true)
                                     (showXAxis true))]
                       (.. chart -xAxis 
                           (axisLabel "x-axis") 
                           (tickFormat (.format js/d3 ",r")))
                       (.. chart -yAxis 
                           (axisLabel "y-axis") 
                           (tickFormat (.format js/d3 ",r")))

                       (let [my-data [{:x 1 :y 5} {:x 2 :y 3} {:x 3 :y 4} {:x 4 :y 1} {:x 5 :y 2}]]

                         (.. js/d3 (select "#d3-node svg")
                             (datum (clj->js [{:values my-data
                                               :key "my-red-line"
                                               :color "red"
                                               }]))
                             (call chart)))))))

(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
