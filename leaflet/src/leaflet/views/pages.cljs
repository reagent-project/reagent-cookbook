(ns leaflet.views.pages
  (:require [leaflet.views.home-page :refer [home-page]]
            [leaflet.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
