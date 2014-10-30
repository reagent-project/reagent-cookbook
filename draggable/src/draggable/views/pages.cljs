(ns draggable.views.pages
  (:require [draggable.views.home-page :refer [home-page]]
            [draggable.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
