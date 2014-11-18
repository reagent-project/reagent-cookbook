(ns d3.views.pages
  (:require [d3.views.home-page :refer [home-page]]
            [d3.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
