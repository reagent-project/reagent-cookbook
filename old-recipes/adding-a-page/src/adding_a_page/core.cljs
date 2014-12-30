(ns adding-a-page.core
  (:require [reagent.core :as reagent]
            [adding-a-page.session :as session :refer [global-state]]
            [adding-a-page.routes :as routes]
            [adding-a-page.views.common :as common]))

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
