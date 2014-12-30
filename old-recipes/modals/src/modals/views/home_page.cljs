(ns modals.views.home-page
  (:require [reagent-modals.modals :as reagent-modals]))

(defn modal-example []
  (reagent-modals/modal [:div "some message to the user!"]))

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [reagent-modals/modal-window]
   [:div.btn.btn-primary {:on-click modal-example} "My Modal"]
   ])
