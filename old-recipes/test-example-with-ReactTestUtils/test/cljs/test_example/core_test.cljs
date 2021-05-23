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
