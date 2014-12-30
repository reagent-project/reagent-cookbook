(ns morris.views.pages
  (:require [morris.views.home-page :refer [home-page]]
            [morris.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
