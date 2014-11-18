(ns d3.core
  (:require [reagent.core :as reagent :refer [atom]]
            [d3.session :as session :refer [get-state]]
            [d3.routes :as routes]
            [d3.views.common :as common]))

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
