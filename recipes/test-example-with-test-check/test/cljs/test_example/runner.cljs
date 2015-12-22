(ns test-example.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [test-example.core-test]))

(doo-tests 'test-example.core-test)
