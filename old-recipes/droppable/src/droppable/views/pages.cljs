(ns droppable.views.pages
  (:require [droppable.views.home-page :refer [home-page]]
            [droppable.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
