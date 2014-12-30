(ns bootstrap-modal.core
    (:require [reagent.core :as reagent]
              [reagent-modals.modals :as reagent-modals]))

(defn modal-window-button []
  [:div.btn.btn-primary {:on-click #(reagent-modals/modal! [:div "some message to the user!"])} 
   "My Modal"])

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [reagent-modals/modal-window]
   [modal-window-button]
   ])

(reagent/render-component [home]
                          (.getElementById js/document "app"))
