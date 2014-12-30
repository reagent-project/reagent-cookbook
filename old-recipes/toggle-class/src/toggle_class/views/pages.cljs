(ns toggle-class.views.pages
  (:require [toggle-class.views.home-page :refer [home-page]]
            [toggle-class.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
