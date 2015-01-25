# Problem

You want to add [DataTables](http://www.datatables.net/) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

[Demo](http://rc-data-tables2.s3-website-us-west-1.amazonaws.com/)

We are going to follow this [example](http://www.datatables.net/examples/basic_init/zero_configuration.html).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add table element with unique id to `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`

#### Step 1: Create a new project

```
$ lein new rc data-tables
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION \/ -->
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- DataTables -->
    <link rel="stylesheet" href="//cdn.datatables.net/1.10.3/css/jquery.dataTables.min.css">
    <script src="//cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
    <script src="//cdn.datatables.net/plug-ins/9dcbecd42ad/integration/bootstrap/3/dataTables.bootstrap.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Add table element with unique id to `home`

Navigate to `src/cljs/data_tables/core.cljs`.

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div.row
    [:div.col-md-6
     [:table#example.table.table-striped.table-bordered {:cell-spacing "0" :width "100%"}
      [:thead
       [:tr [:th "Name"]
        [:th "Age"]]]
      [:tbody
       [:tr [:td "Matthew"]
        [:td "26"]]
       [:tr [:td "Anna"]
        [:td "24"]]
       [:tr [:td "Michelle"]
        [:td "42"]]
       [:tr [:td "Frank"]
        [:td "46"]]]]]]
   ])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
$(document).ready(function() {
    $('#example').DataTable();
} );
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (.ready (js/$ js/document) (fn []
                               (.DataTable (js/$ "#example"))
                               )))
```

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:component-function home
                         :component-did-mount home-did-mount}))
```

#### Step 6: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
```

# Usage

Compile cljs files.

```
$ lein cljsbuild once
```

Start a server.

```
$ lein ring server
```
