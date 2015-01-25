(ns autocomplete.core
  (:require [reagent.core :as reagent]))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div.ui-widget
    [:label {:for "tags"} "Programming Languages: "]
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

(defn home-component []
  (reagent/create-class {:component-function home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
