(ns bootstrap-image-gallery.core
    (:require [reagent.core :as reagent]))

(defn fruit [file-name file-type]
  [:a {:href (str "img/" file-name "." file-type) :title name :data-gallery ""}
   [:img {:src (str "img/thumbnails/" file-name "." file-type) :alt file-name}]])

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#links
    [fruit "banana" "jpg"]
    [fruit "apple" "jpg"]
    [fruit "orange" "jpg"]
    ]])

(reagent/render-component [home]
                          (.getElementById js/document "app"))
