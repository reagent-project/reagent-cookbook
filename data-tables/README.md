# Problem

You want to use [DataTables](http://www.datatables.net/) to display data in your [reagent](https://github.com/holmsand/reagent) webapp.

# Solution

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed data-tables
```

## Add DataTables files to index.html

cd into your newly created project.

Getting started with DataTables is as simple as including two files in `resoucres/index.html`, the CSS styling and the DataTables script itself. These two files are available on the DataTables CDN:

* //cdn.datatables.net/1.10.3/css/jquery.dataTables.min.css
* //cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js

We want to make sure to add the CSS cdn and the DataTables cdn *after* jQuery.  Our `resources/index.html` file should look something like this:

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>data-tables</title>
  </head>
  <body class="container">

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- Font Awesome -->
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">

<!-- ATTENTION \/ -->
	<!-- DataTables -->
    <link rel="stylesheet" href="//cdn.datatables.net/1.10.3/css/jquery.dataTables.min.css">
    <script src="//cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
<!-- ATENTION /\ -->

    <!-- CSS -->
    <link rel="stylesheet" href="css/screen.css">
    <!-- Clojurescript -->
    <script src="/js/app.js"></script>

  </body>
</html>
```

## Familiarize yourself with directory layout

Now, let's briefly take a look at the directory layout of our reagent webapp.

```
dev/
    user.clj                --> functions to start server and browser repl (brepl)
    user.cljs               --> enabling printing to browser's console when connected through a brepl

project.clj                 --> application summary and setup

resources/
    index.html              --> this is the html for your application
    public/                 --> this is where assets for your application will be stored

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

We can see that there are two views:

* about_page.cljs
* home_page.cljs

## Adding DataTables to home-page component

I think we should add DataTables to the home page, but first, let's take a look at what is already there.

```clojure
(ns data-tables.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   ])
```

### Converting javascript function to clojurescript

Ok, the DataTables [zero configuration](http://www.datatables.net/examples/basic_init/zero_configuration.html) guide says to include the following javascript:

```javascript
$(document).ready(function() {
    $('#example').DataTable();
} );
```

Let's convert this to clojurescript.

```clojure
(.ready (js/$ js/document) (fn []
                               (.DataTable (js/$ "#example"))
                               ))
```

### Create a table with id "#example"

Basically, this function applies the `.DataTable()` method on whichever html element has an id of `"#example"`.  Let's create a table with an id of "#example" in the `home-page` reagent component.

```clojure
(ns data-tables.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]

;; ATTENTION \/
   [:table#example.table.table-striped.table-bordered {:cell-spacing "0" :width "100%"}
    [:thead
     [:tr [:th "Name"]
      [:th "Age"]]]
    [:tbody
     [:tr [:td "Matthew"]
      [:td "26"]]
     [:tr [:td "Anna"]
      [:td "14"]]
     [:tr [:td "Michelle"]
      [:td "42"]]
     [:tr [:td "Frank"]
      [:td "37"]]]]
;; ATTENTION /\

   ])

(.ready (js/$ js/document) (fn []
                               (.DataTable (js/$ "#example"))
                               ))
```

### Using react/reagent component lifecycle

However, if we use the `.DataTable()` method as shown, it will fail.  This is because `.DataTable()` will look for an element with the `"#example"` id before reagent has rendered the home-page component.  What we need to do is tap into the react/reagent component lifecycle.  First, let's change `home-page` to `home-render`.

```clojure
...
(defn home-render []
...
```

Next, let'd add our `.DataTable()` method to a *did-mount* component.

```clojure
(ns data-tables.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   [:table#example.table.table-striped.table-bordered {:cell-spacing "0" :width "100%"}
    [:thead
     [:tr [:th "Name"]
      [:th "Age"]]]
    [:tbody
     [:tr [:td "Matthew"]
      [:td "26"]]
     [:tr [:td "Anna"]
      [:td "14"]]
     [:tr [:td "Michelle"]
      [:td "42"]]
     [:tr [:td "Frank"]
      [:td "37"]]]]
    ]])

;; ATTENTION \/
(defn home-did-mount []
  (.ready (js/$ js/document) (fn []
                               (.DataTable (js/$ "#example"))
                               )))
;; ATTENTION /\
```

To make the `home-page` component, which will use both the `home-render` and `home-did-mount` functions, we have to add *reagent* to our namespace.

```clojure
(ns data-tables.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component.

```clojure
(ns data-tables.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   [:table#example.table.table-striped.table-bordered {:cell-spacing "0" :width "100%"}
    [:thead
     [:tr [:th "Name"]
      [:th "Age"]]]
    [:tbody
     [:tr [:td "Matthew"]
      [:td "26"]]
     [:tr [:td "Anna"]
      [:td "14"]]
     [:tr [:td "Michelle"]
      [:td "42"]]
     [:tr [:td "Frank"]
      [:td "37"]]]]
   ])

(defn home-did-mount []
  (.ready (js/$ js/document) (fn []
                               (.DataTable (js/$ "#example"))
                               )))

;; ATTENTION \/
(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
;; ATTENTION /\
```

This is all it takes to add DataTables.

## Usage

To view our app, we need to perform the following steps:

Create a css file.

```
$ lein garden once
```

*Note: if it says "Successful", but you aren't able to type anything into the terminal, hit `Ctrl-c Ctrl-c`.*

Create a javascript file from your clojurescript files.

```
$ lein cljsbuild once
```

Start a repl and then start the server.

```
$ lein repl

user=> (run!)
```

Open a browser and go to *localhost:8080*. You should see your reagent application!
