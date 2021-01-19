(ns add-routing.core
    (:require-macros [secretary.core :refer [defroute]])
    (:import goog.history.Html5History)
    (:require [secretary.core :as secretary]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [reagent.dom :as rdom]
              [reagent.core :as reagent]))

(def app-state (reagent/atom {}))

(defn hook-browser-navigation! []
  (doto (Html5History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (swap! app-state assoc :page :home))

  (defroute "/about" []
    (swap! app-state assoc :page :about))
  
  (hook-browser-navigation!))

(defn home []
  [:div [:h1 "Home Page"]
   [:a {:href "#/about"} "about page"]])

(defn about []
  [:div [:h1 "About Page"]
   [:a {:href "#/"} "home page"]])

(defmulti current-page #(@app-state :page))
(defmethod current-page :home [] 
  [home])
(defmethod current-page :about [] 
  [about])
(defmethod current-page :default [] 
  [:div ])

(defn ^:export main []
  (app-routes)
  (rdom/render [current-page]
                  (.getElementById js/document "app")))

