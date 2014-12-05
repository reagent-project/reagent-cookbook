(ns autocomplete.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]

   [:div.ui-widget
    [:label {:for "tags"} "Tags: "]
    [:input#tags]]

   ])

(defn home-did-mount []
  (js/$ (fn []
          (let [available-tags ["ActionScript"
                                "AppleScript"
                                "Asp"
                                "BASIC"
                                "C"
                                "C++"
                                "Clojure"
                                "COBOL"
                                "ColdFusion"
                                "Erlang"
                                "Fortran"
                                "Groovy"
                                "Haskell"
                                "Java"
                                "JavaScript"
                                "Lisp"
                                "Perl"
                                "PHP"
                                "Python"
                                "Ruby"
                                "Scala"
                                "Scheme"]]
            (.autocomplete (js/$ "#tags") 
                           (clj->js {:source available-tags}))))))

(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
