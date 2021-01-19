(ns bootstrap-modal.core
  (:require
    [reagent.dom :as rdom]
    [reagent.core :as reagent]
    [reagent-modals.modals :as reagent-modals]))

(defn modal-window-button []
  [:div.btn.btn-primary 
   {:on-click #(reagent-modals/modal! [:div "some message to the user!"])} 
   "My Modal"])

(defn home []
  [:div
   [reagent-modals/modal-window]
   ;; ATTENTION \/
   [modal-window-button]
   ;; ATTENTION /\
   ])

(defn ^:export main []
  (rdom/render [home]
                  (.getElementById js/document "app")))

