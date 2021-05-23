(ns test-example.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop :include-macros true]
            [clojure.test.check.clojure-test :refer-macros [defspec]]))

(defspec first-element-is-min-after-sorting ;; the name of the test
  100 ;; the number of iterations for test.check to test
  (prop/for-all [v (gen/not-empty (gen/vector gen/int))]
                (= (apply min v)
                   (first (sort v)))))
