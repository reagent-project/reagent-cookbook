# Problem

You want to add [DataTables](http://www.datatables.net/) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

We are going to follow this [example](http://www.datatables.net/examples/basic_init/zero_configuration.html).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add table element with unique id to `home-render`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home-render` and `home-did-mount` to create a reagent component called `home`
6. Add externs

#### Step 1: Create a new project

```
$ lein new rc data-tables
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>

    <!-- ATTENTION \/ -->
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- DataTables -->
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.3/css/jquery.dataTables.min.css">
    <script src="http://cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
    <script src="http://cdn.datatables.net/plug-ins/9dcbecd42ad/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
    <!-- ATTENTION /\ -->

    <script src="js/compiled/app.js"></script>
    <script>data_tables.core.main();</script>
  </body>
</html>
```

#### Step 3: Add table element with unique id to `home-render`

Navigate to `src/cljs/data_tables/core.cljs`.

```clojure
(defn home-render []
  [:table.table.table-striped.table-bordered 
   {:cell-spacing "0" :width "100%"}

   [:thead>tr 
    [:th "Name"]
    [:th "Age"]]

   [:tbody
    [:tr 
     [:td "Matthew"]
     [:td "26"]]

    [:tr 
     [:td "Anna"]
     [:td "24"]]
    
    [:tr 
     [:td "Michelle"]
     [:td "42"]]
    
    [:tr 
     [:td "Frank"]
     [:td "46"]]
    ]])
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
  (.ready (js/$ js/document) 
          (fn []
            (.DataTable (js/$ "#example")))))
```

The `.ready` method is used to assure that the DOM node exists on the page before executing the `.DataTable` method. However, since we are tapping into the did-mount lifecycle of the component, we are already assured that the component will exist. In addition, rather than using jQuery to select the element by id, we can get the DOM node directly using React/Reagent.  Let's refactor the code as follows:

```clojure
(defn home-did-mount [this]
  (.DataTable (js/$ (reagent/dom-node this))))
```


#### Step 5: Use `home-render` and `home-did-mount` to create a reagent component called `home`

```clojure
(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))
```

#### Step 6: Add externs

For advanced compilation, we need to protect `$.DataTable` from getting renamed. Add an `externs.js` file.

```js
var $ = function(){};
$.DataTable = function(){};
```

Open `project.clj` and add a reference to the externs in the cljsbuild portion.

```clojure
:externs ["externs.js"]
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
