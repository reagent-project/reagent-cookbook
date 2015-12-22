# Reagent Cookbook

![Reagent-Project](logo-rounded.jpg)

The goal of this repo is to provide recipes for how to accomplish specific tasks in a [reagent](https://github.com/reagent-project/reagent) webapp.

For updates, follow us on twitter: [@ReagentProject](https://twitter.com/ReagentProject). Please include `#reagent #cljs` when tweeting about reagent.

For video tutorials, [subscribe](https://www.youtube.com/channel/UC1UP5LiNNNf0a45dA9eDA0Q) to us on youtube.

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
* Testing
    * [test-example with lein-doo](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example)
    * [test-example with lein-doo and ReactTestUtils](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example-with-ReactTestUtils)
	* [test-example with lein-doo and test.check](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/test-example-with-test-check)
* Validation
    * [input validation (color-coded)](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/input-validation)

If you would like to see a *demo* of a recipe, clone this repo and open the recipe's index.html file.

## Common Starting Point for Recipes

The starting point for reagent-cookbook recipes is [reagent-cookbook-template](https://github.com/gadfly361/reagent-cookbook-template).

```
$ lein new rc <name of recipe>
```

Note: reagent-cookbook-template was made specifically for following along with recipes.  If you are interested in starting a new reagent application with some batteries included, then [reagent-template](https://github.com/reagent-project/reagent-template) provides a good starting configuration: `$ lein new reagent <name of app>`.

## Contributing

Recipes are welcomed!  Please fork, branch, and submit a pull request.

Also, I would love a PR for:

* Adding the right externs for advanced compilation of the *nvd3* recipe (it's inside old-recipes for now)
* Adding the right externs for advanced compilation of the *mermaid* recipe (it's inside old-recipes for now)

## LICENSE

Copyright © 2015 Matthew Jaoudi

Distributed under the The MIT License (MIT).
