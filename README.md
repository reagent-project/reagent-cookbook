# ![Reagent-Project](logo-rounded.jpg) Reagent Cookbook

The goal of this repo is to provide recipes for how to accomplish specific tasks in a [reagent](https://github.com/reagent-project/reagent) webapp.

For updates, follow us on twitter: [@ReagentProject](https://twitter.com/ReagentProject)
For video tutorials, [subscribe](https://www.youtube.com/channel/UC1UP5LiNNNf0a45dA9eDA0Q) to us on youtube.

__Spotlight__

* [morris](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/morris) ([demo](http://rc-morris.s3-website-us-west-1.amazonaws.com/)) ([video](https://www.youtube.com/watch?v=2vq7ZfT7m1o))
* [Tic Tac Toe](https://github.com/gadfly361/reagent-tic-tac-toe) ([demo](http://rc-tic-tac-toe.s3-website-us-west-1.amazonaws.com/))

## Recipes

* __General Tasks__
    * [adding a page](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/adding-a-page) ([demo](http://rc-adding-a-page.s3-website-us-east-1.amazonaws.com/))
	* [adding a page from markdown](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/page-from-markdown) ([demo](http://rc-page-from-markdown.s3-website-us-west-1.amazonaws.com/))
	* [data from ajax](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/data-from-ajax)
	* [modal bootstrap window](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/modals) ([demo](http://rc-modals.s3-website-us-west-1.amazonaws.com/))
	* [toggle class](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/toggle-class) ([demo](http://rc-toggle-class.s3-website-us-west-1.amazonaws.com/))
* __Connecting to a database__
    * [Firebase](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/firebase)
* __Adding external javascript libraries__
    * Calendars
        * dropdown calendar: [bootstrap-datepicker](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/bootstrap-datepicker) ([demo](http://rc-bootstrap-datepicker.s3-website-us-west-1.amazonaws.com/))
	* Charting
        * [d3](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/d3) ([demo](http://rc-d3.s3-website-us-west-1.amazonaws.com/))
        * [morris](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/morris) ([demo](http://rc-morris.s3-website-us-west-1.amazonaws.com/)) ([video](https://www.youtube.com/watch?v=2vq7ZfT7m1o))
    * Tables
        * [DataTables](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/data-tables) ([demo](http://rc-data-tables.s3-website-us-west-1.amazonaws.com/))
    * jQuery UI
        * [autocomplete](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/autocomplete) ([demo](http://rc-autocomplete.s3-website-us-west-1.amazonaws.com/))
        * [draggable element](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/draggable) ([demo](http://rc-draggable.s3-website-us-west-1.amazonaws.com/))
        * [droppable element](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/droppable) ([demo](http://rc-droppable.s3-website-us-west-1.amazonaws.com/))
        * [sortable portlets](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/sortable-portlets) ([demo](http://rc-sortable-portlets.s3-website-us-west-1.amazonaws.com/))
    * Maps
        * [Leaflet](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/leaflet) ([demo](http://rc-leaflet.s3-website-us-west-1.amazonaws.com/))
        * [Google Maps](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/google-maps) ([demo](http://rc-google-maps.s3-website-us-west-1.amazonaws.com/))
* __Styling__
    * [Sticky Footer](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/sticky-footer) ([demo](http://rc-sticky-footer.s3-website-us-west-1.amazonaws.com/))

## Example Applications

* [Tic Tac Toe](https://github.com/gadfly361/reagent-tic-tac-toe) ([demo](http://rc-tic-tac-toe.s3-website-us-west-1.amazonaws.com/))

## Common Starting Point for Recipes

**ATTENTION** The current recipes start with [reagent-seed](https://github.com/gadfly361/reagent-seed). However, everything will eventually be converted to start with [reagent-template](https://github.com/reagent-project/reagent-template).
To help improve clarity of the recipes, we are in the process of adding a barebones profile to reagent-template. Once that is ready, I will make the transition.

For your reference, the current recipes are based on reagent-seed, which has the following directory layout:

```
dev/
    user.clj                --> functions to start server and browser repl (brepl)
    user.cljs               --> enabling printing to browser's console when connected through a brepl

project.clj                 --> application summary and setup

resources/
    public/                 --> this is where assets for your application will be stored
        index.html          --> this is the html for your application

src/example/
    core.cljs               ---> main reagent component for application
    css/
        screen.clj          ---> main css file using Garden
    routes.cljs             ---> defining routes using Secretary
    session.cljs            ---> contains atom with application state
    views/
        about_page.cljs     ---> reagent component for the about page
    	common.cljs         ---> common reagent components to all page views
    	home_page.cljs      ---> reagent component for the home page
    	pages.cljs          ---> map of page names to their react/reagent components
```

This template has two views:

* `home-page` located in `src/example/views/home_page.cljs`, and
* `about-page` located in `src/example/views/about_page.cljs`.

The `home-page` view will be the typical starting point for recipes using the reagent-seed template.

## Contributing

Well-documented recipes are welcomed!  Please fork, branch, and submit a pull request.  Also, give me a shout if you have an example of a Reagent application.
