(ns autocomplete.core
  (:require [reagent.core :as reagent]))


(defonce app-state
  (reagent/atom {}))


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


(defn page-render [ratom]
  [:div.ui-widget
   [:label {:for "tags"} "Programming Languages: "]
   [:input#tags]])

(defn page-did-mount []
  (js/$ (fn []
          (.autocomplete (js/$ "#tags")
                         (clj->js {:source tags})))))

(defn page [ratom]
  (reagent/create-class {:reagent-render      #(page-render ratom)
                         :component-did-mount page-did-mount}))


(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (reload))
