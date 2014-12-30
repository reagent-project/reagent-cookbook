(ns modals.core
  (:require [reagent.core :as reagent]
            [modals.session :as session :refer [global-state]]
            [modals.routes :as routes]
            [modals.views.common :as common]))

(defn page-render []
  [:div.container
   [common/header]
   [(global-state :current-page)]])

(defn page-component [] 
  (reagent/create-class {:component-will-mount routes/app-routes
                         :render page-render}))

;; initialize app
(reagent/render-component [page-component]
                          (.getElementById js/document "app"))
