(ns modals.views.pages
  (:require [modals.views.home-page :refer [home-page]]
            [modals.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
