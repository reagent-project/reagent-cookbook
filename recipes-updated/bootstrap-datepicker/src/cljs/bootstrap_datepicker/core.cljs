(ns bootstrap-datepicker.core
  (:require [reagent.core :as reagent]))


(defonce app-state
  (reagent/atom {}))


(defn page-render [ratom]
  [:input {:type        "text"
           :placeholder "click to show datepicker"}])

(defn page-did-mount [this]
  (.datepicker
   (js/$ (reagent/dom-node this))
   (clj->js {:format "dd/mm/yyyy"})))

(defn page [ratom]
  (reagent/create-class
   {:render              #(page-render ratom)
    :component-did-mount #(page-did-mount %)}))


(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (reload))
