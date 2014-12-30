(ns page-from-markdown.util.macros
  (:use [markdown.core :only [md-to-html-string]]))

(defmacro defmd
  [symbol-name html-name]
  (let [base "resources/public/"
        content  (md-to-html-string (slurp (str base html-name)))]
    `(defn ~symbol-name []
       [:div (elem/dangerous :div '~content)])))

;; Note: elem/dangerous is a clojurescript function that we need to include whenever we use this macro!
