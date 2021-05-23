# Problem

You want to add [cljs.test](https://github.com/clojure/clojurescript/blob/master/src/main/cljs/cljs/test.cljs) and [doo](https://github.com/bensu/doo) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

*Steps*

1. Create a new project
2. Add lein-doo to `project.clj` :plugins vector
3. Add `test` build to `project.clj`
4. Add a test file with a `deftest`
5. Add a test runner

#### Step 1: Create a new project

```
$ lein new rc test-example
```

#### Step 2: Add lein-doo to `project.clj` :plugins vector

```clojure
[lein-doo "0.1.6"]
```

#### Step 3: Add `test` build to `project.clj

```clojure
{:id "test"
 :source-paths ["src/cljs" "test/cljs"]
 :compiler {:output-to "resources/public/js/compiled/test.js"
            :main test-example.runner
            :optimizations :none}}
```

#### Step 4: Add a test file with a `deftest`

Create the file *test/cljs/test\_example/core\_test.cljs* and add the following:

```clojure
(ns test-example.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
```

#### Step 5: Add a test runner

Create the file *test/cljs/test_example/runner.cljs* and add the following:

```clojure
(ns test-example.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [test-example.core-test]))

(doo-tests 'test-example.core-test)
```

# Usage

Run the tests.

```
$ lein clean
$ lein doo phantom test once
```

The above command assumes that you have [phantomjs](https://www.npmjs.com/package/phantomjs) installed. However, please note that [doo](https://github.com/bensu/doo) can be configured to run cljs.test in many other JS environments (chrome, ie, safari, opera, slimer, node, rhino, or nashorn). 
