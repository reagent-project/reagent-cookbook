(ns morris.core
  (:require [reagent.core :as reagent]
            [morris.session :as session :refer [global-state]]
            [morris.routes :as routes]
            [morris.views.common :as common]))

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
