(ns autocomplete.views.pages
  (:require [autocomplete.views.home-page :refer [home-page]]
            [autocomplete.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
