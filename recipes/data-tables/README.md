# Problem

You want to use [DataTables](http://www.datatables.net/) to display data in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to create a table using DataTables' zero configuration [example](http://www.datatables.net/examples/basic_init/zero_configuration.html).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add DataTables files to index.html
* Add DataTables to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.

Affected files:

* `resources/public/index.html`
* `src/data_tables/views/home_page.cljs`

## Create a reagent project

```
$ lein new reagent-seed data-tables
```

## Add DataTables files to index.html

Add DataTables to your `resources/public/index.html` file.

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
    <script src="//cdn.datatables.net/plug-ins/9dcbecd42ad/integration/bootstrap/3/dataTables.bootstrap.js"></script>
<!-- ATENTION /\ -->

    <!-- CSS -->
    <link rel="stylesheet" href="css/screen.css">
    <!-- Clojurescript -->
    <script src="/js/app.js"></script>

  </body>
</html>
```

## Adding DataTables to home-page component

Navigate to `src/data_tables/views/home_page.cljs`.  To add DataTables, we need to create a table element with a unique id (`#example`).

```clojure
(ns data-tables.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   
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
```

## Convert javascript to clojurescript

This is the javascript we want to include:

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

Basically, this function applies the `.DataTable()` method on whichever html element has an id of `"#example"`.

### Change home-page component to home-render function

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run.  What we need to do is tap into the react/reagent component lifecycle. First, let's change the `home-page` component to `home-render` function.

```clojure
...
(defn home-render []
...
```

### Create did-mount function

Next, let'd add our `.DataTable()` method to a *did-mount* component.

```clojure
(ns data-tables.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
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

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns data-tables.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

```clojure
(ns data-tables.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
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

# Usage

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
