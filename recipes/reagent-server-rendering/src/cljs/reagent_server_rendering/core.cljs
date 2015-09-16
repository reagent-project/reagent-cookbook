(ns reagent-server-rendering.core
    (:require [reagent.core :as reagent]))

(defn home-page []
  [:div [:h2 "Welcome to reagent-server-rendering"]
   [:div [:a {:href "/about"} "go to about page"]]])

(defn about-page []
  [:div [:h2 "About reagent-server-rendering"]
   [:div [:a {:href "/"} "go to the home page"]]])

(def pages
  {"home"  home-page
   "about" about-page})

(defn ^:export render-page [page-id]
  (reagent/render-to-string [(get pages page-id)]))

(defn ^:export main [page-id]
  (reagent/render [(get pages page-id)] (.getElementById js/document "app")))

