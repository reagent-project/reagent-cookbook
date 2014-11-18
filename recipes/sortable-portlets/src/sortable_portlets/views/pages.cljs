(ns sortable-portlets.views.pages
  (:require [sortable-portlets.views.home-page :refer [home-page]]
            [sortable-portlets.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
