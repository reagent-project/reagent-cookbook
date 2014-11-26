(ns d3.core
  (:require [reagent.core :as reagent]
            [d3.session :as session :refer [global-state]]
            [d3.routes :as routes]
            [d3.views.common :as common]))

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
