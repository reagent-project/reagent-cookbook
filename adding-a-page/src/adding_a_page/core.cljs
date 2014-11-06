(ns adding-a-page.core
  (:require [reagent.core :as reagent :refer [atom]]
            [adding-a-page.session :as session :refer [get-state]]
            [adding-a-page.routes :as routes]
            [adding-a-page.views.common :as common]))

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
