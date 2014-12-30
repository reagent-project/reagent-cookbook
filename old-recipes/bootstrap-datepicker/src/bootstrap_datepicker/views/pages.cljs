(ns bootstrap-datepicker.views.pages
  (:require [bootstrap-datepicker.views.home-page :refer [home-page]]
            [bootstrap-datepicker.views.about-page :refer [about-page]]))

(def pages {:home-page home-page
            :about-page about-page})
