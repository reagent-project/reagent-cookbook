# Old recipes based on [reagent-seed](https://github.com/gadfly361/reagent-seed)

* __General Tasks__
    * [adding a page](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/adding-a-page) ([demo](http://rc-adding-a-page.s3-website-us-east-1.amazonaws.com/))
	* [adding a page from markdown](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/page-from-markdown) ([demo](http://rc-page-from-markdown.s3-website-us-west-1.amazonaws.com/))
	* [data from ajax](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/data-from-ajax)
	* [modal bootstrap window](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/modals) ([demo](http://rc-modals.s3-website-us-west-1.amazonaws.com/))
	* [toggle class](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/toggle-class) ([demo](http://rc-toggle-class.s3-website-us-west-1.amazonaws.com/))
* __Connecting to a database__
    * [Firebase](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/firebase)
* __Adding external javascript libraries__
    * Calendars
        * dropdown calendar: [bootstrap-datepicker](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/bootstrap-datepicker) ([demo](http://rc-bootstrap-datepicker.s3-website-us-west-1.amazonaws.com/))
	* Charting
        * [d3](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/d3) ([demo](http://rc-d3.s3-website-us-west-1.amazonaws.com/))
        * [morris](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/morris) ([demo](http://rc-morris.s3-website-us-west-1.amazonaws.com/)) ([video](https://www.youtube.com/watch?v=2vq7ZfT7m1o))
    * Tables
        * [DataTables](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/data-tables) ([demo](http://rc-data-tables.s3-website-us-west-1.amazonaws.com/))
    * jQuery UI
        * [autocomplete](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/autocomplete) ([demo](http://rc-autocomplete.s3-website-us-west-1.amazonaws.com/))
        * [draggable element](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/draggable) ([demo](http://rc-draggable.s3-website-us-west-1.amazonaws.com/))
        * [droppable element](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/droppable) ([demo](http://rc-droppable.s3-website-us-west-1.amazonaws.com/))
        * [sortable portlets](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/sortable-portlets) ([demo](http://rc-sortable-portlets.s3-website-us-west-1.amazonaws.com/))
    * Maps
        * [Leaflet](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/leaflet) ([demo](http://rc-leaflet.s3-website-us-west-1.amazonaws.com/))
        * [Google Maps](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/google-maps) ([demo](http://rc-google-maps.s3-website-us-west-1.amazonaws.com/))
* __Styling__
    * [Sticky Footer](https://github.com/reagent-project/reagent-cookbook/tree/master/old-recipes/sticky-footer) ([demo](http://rc-sticky-footer.s3-website-us-west-1.amazonaws.com/))

# Common Starting Poing for Old Recipes

For your reference, the old recipes are based on reagent-seed, which has the following directory layout:

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
