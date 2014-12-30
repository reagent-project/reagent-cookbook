(ns google-maps.views.pages
  (:require [google-maps.views.home-page :refer [home-page]]
            [google-maps.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
