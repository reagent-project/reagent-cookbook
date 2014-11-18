(ns sortable-portlets.core
  (:require [reagent.core :as reagent :refer [atom]]
            [sortable-portlets.session :as session :refer [get-state]]
            [sortable-portlets.routes :as routes]
            [sortable-portlets.views.common :as common]))

(defn page-render []
  [:div
   [common/header]
   [(get-state :current-page)]])

(defn page-component [] 
  (reagent/create-class {:component-will-mount routes/app-routes
                         :render page-render}))

;; initialize app
(reagent/render-component [page-component]
                          (.getElementById js/document "app"))
