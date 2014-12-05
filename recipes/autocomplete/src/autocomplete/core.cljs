(ns autocomplete.core
  (:require [reagent.core :as reagent]
            [autocomplete.session :refer [global-state]]
            [autocomplete.routes :as routes]
            [autocomplete.views.common :as common]))

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
