(ns firebase.views.pages
  (:require [firebase.views.home-page :refer [home-page]]
            [firebase.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
