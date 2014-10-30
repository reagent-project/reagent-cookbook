(ns d3.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]

   [:div#d3-node [:svg ]]

   ])

(defn home-did-mount []
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
          (datum #js [#js {:values (clj->js my-data)
                           :key "Text Data"
                           :color "red"
                           } ])
          (call chart)))))

(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
