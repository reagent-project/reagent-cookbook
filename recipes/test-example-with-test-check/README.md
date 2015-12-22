# Problem

You want to add [cljs.test](https://github.com/clojure/clojurescript/blob/master/src/main/cljs/cljs/test.cljs), [doo](https://github.com/bensu/doo), and [test.check](https://github.com/clojure/test.check) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

*Steps*

1. Follow all of the instructions in the [test-example](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example) recipe.
2. Add [test.check](https://github.com/clojure/test.check) to `project.clj` :dependencies vector
3. Add a test that will use test.check's `defspec` macro

#### Step 1: Follow all of the instructions in the [test-example](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example) recipe.

#### Step 2: Add [test.check](https://github.com/clojure/test.check) to `project.clj` :dependencies vector

```clojure
[org.clojure/test.check "0.9.0"]
```

#### Step 3: Add a test that will use test.check's `defspec` macro

Navigate to `test/cljs/test_example/core_test.cljs` and make it look like the following.

```clojure
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
```

# Usage

Run the tests.

```
$ lein clean
$ lein doo phantom test once
```

The above command assumes that you have [phantomjs](https://www.npmjs.com/package/phantomjs) installed. However, please note that [doo](https://github.com/bensu/doo) can be configured to run cljs.test in many other JS environments (chrome, ie, safari, opera, slimer, node, rhino, or nashorn). 
