(ns bootstrap-modal.core
  (:require [reagent.core :as reagent]
            [reagent-modals.modals :as reagent-modals]))

(defn modal-window-button []
  [:div.btn.btn-primary 
   {:on-click #(reagent-modals/modal! [:div "some message to the user!"])} 
   "My Modal"])

(defn home []
  [:div
   [reagent-modals/modal-window]
   ;; ATTNETION \/
   [modal-window-button]
   ;; ATTENTION /\
   ])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

