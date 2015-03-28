# Reagent Cookbook

![Reagent-Project](logo-rounded.jpg)

The goal of this repo is to provide recipes for how to accomplish specific tasks in a [reagent](https://github.com/reagent-project/reagent) webapp.

For updates, follow us on twitter: [@ReagentProject](https://twitter.com/ReagentProject)

For video tutorials, [subscribe](https://www.youtube.com/channel/UC1UP5LiNNNf0a45dA9eDA0Q) to us on youtube.

## Recipes

* Bootstrap
    * [bootstrap modal window](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/bootstrap-modal) | [Demo](http://rc-bootstrap-modal.s3-website-us-west-1.amazonaws.com/) | [Video](https://www.youtube.com/watch?v=qRJiAp92TPg)
    * [bootstrap-datepicker](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/bootstrap-datepicker) | [Demo](http://rc-bootstrap-datepicker2.s3-website-us-west-1.amazonaws.com/) | [Video](https://www.youtube.com/watch?v=kSzb8YHZV9Q)
* Charting
    * [highcharts](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/highcharts) | [Demo](http://rc-highcharts.s3-website-us-west-1.amazonaws.com/)
    * [nvd3](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/nvd3) | [Demo](http://rc-nvd3.s3-website-us-west-1.amazonaws.com/)
    * [mermaid](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/mermaid) | [Demo](http://rc-mermaid.s3-website-us-west-1.amazonaws.com/)
    * [morris](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/morris) | [Demo](http://rc-morris2.s3-website-us-west-1.amazonaws.com/) | [Video](https://www.youtube.com/watch?v=0KwPtunz-Qo)
* Images
    * [google-street-view](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/google-street-view) | [Demo](http://rc-google-street-view.s3-website-us-east-1.amazonaws.com/)
* jQuery UI
    * [autocomplete](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/autocomplete) | [Demo](http://rc-autocomplete2.s3-website-us-west-1.amazonaws.com/)
    * [draggable element](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/draggable) | [Demo](http://rc-draggable2.s3-website-us-west-1.amazonaws.com/)
    * [droppable element](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/droppable) | [Demo](http://rc-droppable2.s3-website-us-west-1.amazonaws.com/)
    * [sortable portlets](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/sortable-portlets) | [Demo](http://rc-sortable-portlets2.s3-website-us-west-1.amazonaws.com/)
* Maps
    * [Leaflet](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/leaflet) | [Demo](http://rc-leaflet2.s3-website-us-west-1.amazonaws.com/)
    * [Google Maps](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/google-maps) | [Demo](http://rc-google-maps2.s3-website-us-west-1.amazonaws.com/)
* Misc.
    * [adding a page](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/adding-a-page) | [Demo](http://rc-adding-a-page2.s3-website-us-west-1.amazonaws.com/) | [Video](https://www.youtube.com/watch?v=D7uwDUUngy0)
	* [toggle class](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/toggle-class) | [Demo](http://rc-toggle-class2.s3-website-us-west-1.amazonaws.com/) | [Video](https://www.youtube.com/watch?v=WcMrLhW20zg)
* Sidebar
    * [Simple Sidebar](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/simple-sidebar) | [Demo](http://rc-simple-sidebar.s3-website-us-west-1.amazonaws.com/)
* Tables
    * [DataTables](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/data-tables) | [Demo](http://rc-data-tables2.s3-website-us-west-1.amazonaws.com/)
* Validation
    * [input validation (color-coded)](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/input-validation) | [Demo](http://rc-input-validation.s3-website-us-west-1.amazonaws.com/)

For the old recipes (mostly) based on reagent-seed, see the [old-recipes](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes) folder.

## Useful Links

* [reagent website](http://reagent-project.github.io/)
* [reagent project - google groups](https://groups.google.com/forum/#!forum/reagent-project)
* [re-frame README and Wiki](https://github.com/Day8/re-frame)
* [Luminus](http://www.luminusweb.net/docs/clojurescript.md)
* Blogs with Reagent:
    * [Yogthos](http://yogthos.net/)
	* [Matt Greer](http://www.mattgreer.org/)
	* [Max Gonzih](http://blog.gonzih.me/)
	* [Dhruv](http://dhruvp.github.io/)
* Meetups
    * [SF Reagent Meetup](http://www.meetup.com/Reagent-Minimalistic-React-for-ClojureScript/)

*Tweet new links to @ReagentProject*

## Example Applications

* [reagent-breakout](https://github.com/city41/reagent-breakout) | [Demo](http://city41.github.io/reagent-breakout/index.html)
* [reagent-phonecat](https://github.com/vvvvalvalval/reagent-phonecat)
* [reagent-tic-tac-toe](https://github.com/gadfly361/reagent-tic-tac-toe) | [Demo](http://rc-tic-tac-toe.s3-website-us-west-1.amazonaws.com/)
* [clojure-quotester](https://github.com/philjackson/clojure-quotester)

*Tweet examples to @ReagentProject*

## Common Starting Point for Recipes

The starting point for reagent-cookbook recipes is [reagent-cookbook-template](https://github.com/gadfly361/reagent-cookbook-template).

```
$ lein new rc <name of recipe>
```

Note: reagent-cookbook-template was made specifically for following along with recipes - it is not meant for production.  If you are interested in starting a new reagent application, then [reagent-template](https://github.com/reagent-project/reagent-template) provides a good starting configuration: `$ lein new reagent <name of app>`.

## Contributing

Recipes are welcomed!  Please fork, branch, and submit a pull request.
