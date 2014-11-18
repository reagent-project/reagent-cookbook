(ns sticky-footer.views.pages
  (:require [sticky-footer.views.home-page :refer [home-page]]
            [sticky-footer.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
