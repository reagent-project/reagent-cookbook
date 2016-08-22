# Reagent Cookbook

![Reagent-Project](logo-rounded.jpg)

The goal of this repo is to provide recipes for how to accomplish specific tasks in a [reagent](https://github.com/reagent-project/reagent) webapp.

For updates, follow us on twitter: [@ReagentProject](https://twitter.com/ReagentProject). Please include `#reagent #cljs` when tweeting about reagent.

## Basics

* [Basic Component](https://github.com/reagent-project/reagent-cookbook/tree/master/basics/basic-component)
* [Component-level State](https://github.com/reagent-project/reagent-cookbook/tree/master/basics/component-level-state)
* [Cursors](https://github.com/reagent-project/reagent-cookbook/tree/master/basics/cursors)

## Recipes

* Animation
    * [mojs animation](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/mojs-animation)
    * [ReactCSSTransitionGroup](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/ReactCSSTransitionGroup)
* Bootstrap
    * [bootstrap modal window](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/bootstrap-modal)
    * [bootstrap-datepicker](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/bootstrap-datepicker)
* Canvas
    * [Canvas fills div](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/canvas-fills-div)
* Charting
    * [highcharts](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/highcharts)
    * [morris](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/morris)
* Images
    * [google-street-view](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/google-street-view)
* jQuery UI
    * [autocomplete](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/autocomplete)
    * [draggable element](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/draggable)
    * [droppable element](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/droppable)
    * [sortable portlets](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/sortable-portlets)
* Maps
    * [Leaflet](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/leaflet)
    * [Google Maps](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/google-maps)
* Misc.
    * [compare argv](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/compare-argv)
    * [Live Markdown Editor](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/markdown-editor)
	* [server-side-rendering](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/reagent-server-rendering)
	* [toggle class](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/toggle-class)
* Routing
    * [add routing](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/add-routing) with secretary
* Sidebar
    * [Simple Sidebar](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/simple-sidebar)
* State
    * [local-storage](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/local-storage) with storage-atom
    * [undo](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/undo) with historian
* Tables
    * [DataTables](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/data-tables)
	* [sortable table](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/sort-table)
	* [filter table] (https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/filter-table)
* Testing
    * [test-example with lein-doo](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example)
    * [test-example with lein-doo and ReactTestUtils](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example-with-ReactTestUtils)
	* [test-example with lein-doo and test.check](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example-with-test-check)
* Validation
    * [input validation (color-coded)](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/input-validation)

## Common Starting Point for Recipes

The starting point for reagent-cookbook recipes is [reagent-cookbook-template](https://github.com/gadfly361/reagent-cookbook-template).

```
$ lein new rc <app-name>
```

The template includes the following:

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>
    <script src="js/app.js"></script>
    <script>app_name.core.main();</script>
  </body>
</html>
```

```clojure
(defproject app-name "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [reagent "0.6.0-rc"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.4-7"]]

  :clean-targets ^{:protect false} ["resources/public/js" "target"]

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "app-name.core/reload"}
     :compiler     {:main                 app-name.core
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/out"
                    :asset-path           "js/out"
                    :source-map-timestamp true}}

    {:id           "prod"
     :source-paths ["src/cljs"]
     :compiler     {:output-to     "resources/public/js/app.js"
                    :optimizations :advanced
                    :pretty-print  false}}]})
```

```clojure
(ns app-name.core
  (:require [reagent.core :as reagent]))


(defonce app-state
  (reagent/atom {}))


(defn page [ratom]
  [:div
   [:h1 "Reagent Cookbook Template"]
   ])


(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (reload))
```

Note: reagent-cookbook-template was made specifically for following along with recipes.  If you are interested in starting a proper reagent application, then checkout:

* [reagent-template](https://github.com/reagent-project/reagent-template)
* [reagent-figwheel](https://github.com/gadfly361/reagent-figwheel)
* [re-frame-template](https://github.com/Day8/re-frame-template)

## Contributing

Recipes are welcomed!  Please fork, branch, and submit a pull request.

## LICENSE

Copyright © 2014-2016 Matthew Jaoudi

Distributed under the The MIT License (MIT).
