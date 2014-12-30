(ns page-from-markdown.views.pages
  (:require [page-from-markdown.views.home-page :refer [home-page]]
            [page-from-markdown.views.about-page :refer [about-page]]
            [page-from-markdown.util.elem :as elem]) ;this is used in defmd macro
            (:require-macros [page-from-markdown.util.macros :refer [defmd]]))

(defmd page-from-markdown "page-from-markdown.md") ; this will return a reagent component

(def pages {:home-page home-page
            :about-page about-page
            :page-from-markdown page-from-markdown
            })
