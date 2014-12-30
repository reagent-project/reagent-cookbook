(ns adding-a-page.views.pages
  (:require [adding-a-page.views.home-page :refer [home-page]]
            [adding-a-page.views.about-page :refer [about-page]]
            [adding-a-page.views.new-page :refer [new-page]]))

(def pages {:home-page home-page
            :about-page about-page
            :new-page new-page})
