(ns draggable.core
  (:require [reagent.core :as reagent]
            [draggable.session :as session :refer [global-state]]
            [draggable.routes :as routes]
            [draggable.views.common :as common]))

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
