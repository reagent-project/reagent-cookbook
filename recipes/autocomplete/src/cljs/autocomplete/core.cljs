(ns autocomplete.core
    (:require [reagent.core :as reagent]))

(defn home-render []
  [:div.ui-widget
   [:label {:for "tags"} "Programming Languages: "]
   [:input#tags]])

(def tags 
  ["ActionScript"
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
   "Scheme"])

(defn home-did-mount []
  (js/$ (fn []
          (.autocomplete (js/$ "#tags") 
                         (clj->js {:source tags})))))

(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

