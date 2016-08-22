(ns bootstrap-modal.core
  (:require [reagent.core :as reagent]
    [reagent-modals.modals :as reagent-modals]))


(defonce app-state
  (reagent/atom {}))


(defn modal-window-button []
  [:div.btn.btn-primary
   {:on-click #(reagent-modals/modal! [:div "some message to the user!"])}
   "My Modal"])


(defn page [ratom]
  [:div
   [reagent-modals/modal-window]
   [modal-window-button]
   ])


(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (reload))
