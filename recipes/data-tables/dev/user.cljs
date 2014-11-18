(ns cljs.user
  (:require [clojure.browser.repl]))

;; This one makes all the Clojure printing functions use console.log,
;; so we can do (prn "Stuff") instead of (.log js/console "Stuff")
;; NOTE: For nice object formatting you may still want to use console.log
;; directly.
(enable-console-print!)
