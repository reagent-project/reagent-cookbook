# Problem

You want to add [cljs.test](https://github.com/clojure/clojurescript/blob/master/src/main/cljs/cljs/test.cljs), [doo](https://github.com/bensu/doo), and [ReactTestUtils](https://facebook.github.io/react/docs/test-utils.html) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

*Steps*

0. Follow all of the instructions in the [test-example](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example) recipe.
1. Add [cljs-react-test](https://github.com/bensu/cljs-react-test) and [dommy](https://github.com/Prismatic/dommy) to `project.clj` :dependencies vector, as well as change react to react-with-addons
2. Add an increment button to `core.cljs`
3. Add a test that will simulate the on-click event

#### Step 1: Follow all of the instructions in the [test-example](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example) recipe.

#### Step 2: Add [cljs-react-test](https://github.com/bensu/cljs-react-test) and [dommy](https://github.com/Prismatic/dommy) to `project.clj` :dependencies vector, as well change react to react-with-addons

```clojure
:dependencies [...
               [reagent "0.5.1":exclusions [cljsjs/react]]
               [cljsjs/react-with-addons "0.13.3-0"]
               [cljs-react-test "0.1.3-SNAPSHOT"]
               [prismatic/dommy "1.1.0"]]
```

#### Step 3: Add an increment button to `core.cljs`

Navigate to `src/cljs/test_example/core.cljs` and add the following button.

```clojure
(ns test-example.core
  (:require [reagent.core :as reagent]))

;; ATTENTION \/
(defonce app-state (reagent/atom {:count 0}))

(defn handle-on-click [app-state]
  (swap! app-state update-in [:count] inc))

(defn increment-button [app-state]
  [:button {:on-click #(handle-on-click app-state)}
   "Increment"])

(defn home []
  [:div
   [increment-button app-state]
   [:div "Current count: " (@app-state :count)]
   ])
;; ATTENTION /\

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))
```

#### Step 4: Add a test that will simulate the on-click event

Navigate to `test/cljs/test_example/core_test.cljs` and make it look like the following.

For this single `deftest`, the use of `use-fixtures` isn't necessary; however, you will likely find this pattern useful when defining multiple `deftest`s.

```clojure
(ns test-example.core-test
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [cljs-react-test.utils :as tu]
            [cljs-react-test.simulate :as sim]
            [dommy.core :as dommy :refer-macros [sel1]]
            [reagent.core :as reagent]
            [test-example.core :as core]))

(def ^:dynamic c)

(use-fixtures :each (fn [test-fn]
                      (binding [c (tu/new-container!)]
                        (test-fn)
                        (tu/unmount! c))))

(deftest increment-button
  (testing "on-click"
    (let [app-state (reagent/atom {:count 0})
          _ (reagent/render [core/increment-button app-state] c)
          node (sel1 c [:button])]
      (is (= 0 (:count @app-state)))
      (sim/click node nil)
      (is (= 1 (:count @app-state))))))
```

# Usage

Run the tests.

```
$ lein clean
$ lein doo phantom test once
```

The above command assumes that you have [phantomjs](https://www.npmjs.com/package/phantomjs) installed. However, please note that [doo](https://github.com/bensu/doo) can be configured to run cljs.test in many other JS environments (chrome, ie, safari, opera, slimer, node, rhino, or nashorn). 
