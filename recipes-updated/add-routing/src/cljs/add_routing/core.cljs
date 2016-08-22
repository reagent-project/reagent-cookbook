(ns add-routing.core
    (:require-macros [secretary.core :refer [defroute]])
    (:import goog.History)
    (:require [secretary.core :as secretary]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [reagent.core :as reagent]))


;; model

(defonce app-state
  (reagent/atom {}))



;; pages

(defn home [ratom]
  [:div [:h1 "Home Page"]
   [:a {:href "#/about"} "about page"]])

(defn about [ratom]
  [:div [:h1 "About Page"]
   [:a {:href "#/"} "home page"]])

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))



;; routes

(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (swap! app-state assoc :page :home))

  (defroute "/about" []
    (swap! app-state assoc :page :about))

  ;; note: this needs to be called after defining routes
  (hook-browser-navigation!))


(defmulti current-page identity)
(defmethod current-page :home [] home)
(defmethod current-page :about [] about)
(defmethod current-page :default [] (fn [_] [:div]))



;; root component

(defn page [ratom]
  (let [page-key (:page @ratom)]
    [(current-page page-key) ratom]))



;; initialize app

(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (app-routes)
  (reload))
