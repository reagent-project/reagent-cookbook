(ns bootstrap-datepicker.core
  (:require [reagent.core :as reagent]
            [bootstrap-datepicker.session :as session :refer [global-state]]
            [bootstrap-datepicker.routes :as routes]
            [bootstrap-datepicker.views.common :as common]))

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
