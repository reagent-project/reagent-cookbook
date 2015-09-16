(ns compare-argv.core
    (:require [reagent.core :as reagent]))



(defn update-highlighter
  "Displays two vals and highlights the most recently updated one"
  [val1 val2]
  (let [last-updated (reagent/atom 0)]
    (reagent/create-class
      {:component-will-update (fn [this new-argv]
                                (let [[_ old-v1 old-v2] (reagent/argv this)
                                      [_ new-v1 new-v2] new-argv]
                                  (reset! last-updated
                                          (cond
                                            (> new-v1 old-v1) 1
                                            (> new-v2 old-v2) 2
                                            :else 0))))
       :reagent-render        (fn [val1 val2]
                                [:div
                                 ; display the current value of each,
                                 ; and highlight which was last updated
                                 [:div (when (= 1 @last-updated) {:class "selectedRow"})
                                  (str "val1 = " val1)]
                                 [:div (when (= 2 @last-updated) {:class "selectedRow"})
                                  (str "val2 = " val2)]])})))


(defn home []
  (let [v1 (reagent/atom 0)
        v2 (reagent/atom 0)]
    (fn []
      [:div
       [:button {:on-click #(swap! v1 inc)} "inc val1"]
       [:button {:on-click #(swap! v2 inc)} "inc val2"]
       [update-highlighter @v1 @v2]])))


(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

