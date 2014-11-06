(ns adding-a-page.routes
    (:require [secretary.core :as secretary :include-macros true :refer [defroute]]
              [adding-a-page.session :as session]
              [adding-a-page.views.pages :as pages]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; ----------
;; History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
;; need to run this after routes have been defined

;; ----------
;; Routes
(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (session/put! :current-page (pages/pages :home-page))
    (session/put! :nav "home"))

  (defroute "/about" []
    (session/put! :current-page (pages/pages :about-page))
    (session/put! :nav "about"))

  (defroute "/new-page" []
    (session/put! :current-page (pages/pages :new-page))
    (session/put! :nav "new"))

  (hook-browser-navigation!)
  )
