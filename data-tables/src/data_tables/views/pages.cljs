(ns data-tables.views.pages
  (:require [data-tables.views.home-page :refer [home-page]]
            [data-tables.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
