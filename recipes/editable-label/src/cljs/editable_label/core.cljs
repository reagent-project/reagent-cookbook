(ns editable-label.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as reagent]))

(defn label-edit [item]
  (let [form            (reagent/atom item)
        edit?           (reagent/atom false)
        input-component (fn []
                          [:input.text
                           {:value          @form
                            :on-change      #(reset! form (-> % .-target .-value))
                            :on-key-press   (fn [e]
                                              (let [enter? (= 13 (.-charCode e))]
                                                (when enter?
                                                  (reset! edit? false)
                                                  (reset! form @form))))
                            :on-mouse-leave #(do (reset! edit? false)
                                                 (reset! form @form))}])]
    (fn [item]
      [:div
       (if-not @edit?
         [:label {:on-double-click #(reset! edit? true)} @form]
         [input-component])])))

(defn home []
  [:div.container
   [:h1 "Editing Labels"]
   [label-edit (str "Double-click to edit")]
   [label-edit (str "Edit me!!")]])

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))
