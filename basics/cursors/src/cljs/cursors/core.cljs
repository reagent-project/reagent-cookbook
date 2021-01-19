(ns cursors.core
    (:require [reagent.core :as reagent]
              [reagent.dom :as rdom]))

(def app-state (reagent/atom {:foo {:bar "Hello, world!"
                                    :baz {:quux "Woot"}}}))

(defn inside-app-state []
  [:div (str "Inside app-state: " @app-state)])

(def foo-cursor (reagent/cursor app-state [:foo]))

(defn inside-foo-cursor []
  [:div (str "Inside foo-cursor: " @foo-cursor)])

(def foobar-cursor (reagent/cursor app-state [:foo :bar]))

(defn inside-foobar-cursor []
  [:div (str "Inside foobar-cursor: " @foobar-cursor)])

(def foobaz-cursor (reagent/cursor app-state [:foo :baz]))

(defn inside-foobaz-cursor []
  [:div (str "Inside foobaz-cursor: " @foobaz-cursor)])

(def foobazquux-cursor (reagent/cursor app-state [:foo :baz :quux]))

(defn inside-foobazquux-cursor []
  [:div (str "Inside foobazquux-cursor: " @foobazquux-cursor)])

(defn home []
  [:div
   [inside-app-state]
   [inside-foo-cursor]
   [inside-foobar-cursor]
   [inside-foobaz-cursor]
   [inside-foobazquux-cursor]
   ])

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))

