(ns adding-a-page.core
  (:require [reagent.core :as reagent]
            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

;; Application State
(def app-state (reagent/atom {}))

(defn put! [k v]
  (swap! app-state assoc k v))

;; Views
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div [:a {:href "#/about"} "go to the about page"]]])

(defn about []
  [:div "This is the about page."
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn current-page-will-mount []
  (put! :current-page home))

(defn current-page-render []
  [(@app-state :current-page)])

(defn current-page []
  (reagent/create-class {:component-will-mount current-page-will-mount
                         :reagent-render current-page-render}))

;; Routes
(secretary/set-config! :prefix "#")

(defroute "/" []
  (put! :current-page home))

(defroute "/about" []
  (put! :current-page about))

;; History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(hook-browser-navigation!)

;; Initialize App
(reagent/render-component [current-page]
                          (.getElementById js/document "app"))
