# Problem

You want to use [bootstrap-datepicker](https://github.com/eternicode/bootstrap-datepicker) to display a dropdown calendar in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to follow this [example](http://runnable.com/UmOlOZbXvZRqAABU/bootstrap-datepicker-example-text-input-with-specifying-date-format2).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add bootstrap-datepicker files to index.html
* Add bootstrap-datepicker to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.

Affected files:

* `resources/public/index.html`
* `src/bootstrap_datepicker/views/home_page.cljs`

## Create a reagent project

```
$ lein new reagent-seed bootstrap-datepicker
```

## Add bootstrap-datepicker files to index.html

Add the bootstrap-datepicker files to your `resources/public/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>bootstrap-datepicker</title>
  </head>
  <body>

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
    <!-- datepicker -->
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker3.min.css">
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.min.js"></script>
<!-- ATTENTION /\ -->

    <!-- CSS -->
    <link rel="stylesheet" href="css/screen.css">
    <!-- Clojurescript -->
    <script src="/js/app.js"></script>

  </body>
</html>

```

## Add bootstrap-datepicker to home-page component

Navigate to `src/bootstrap_datepicker/views/home_page.cljs`. To add a bootstrap-datepicker, we need to create a text input with unique id.

```clojure
(ns bootstrap-datepicker.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
;; ATTENTION \/
   [:input#example1 {:type "text" :placeholder "click to show datepicker"}]
;; ATTENTION /\
   ])
```

## Convert javascript to clojurescript

To add bootstrap-datepicker, this is the javascript we want to include:

```javascript
// When the document is ready
$(document).ready(function () {
    
    $('#example1').datepicker({
        format: "dd/mm/yyyy"
    });
    
});
```

Let's convert this to clojurescript.

```clojure
(.ready (js/$ js/document) 
        (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"}))))
```

### Change home-page component to home-render function

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run.  What we need to do is tap into the react/reagent component lifecycle. First, let's change the `home-page` component to `home-render` function.

```clojure
...
(defn home-render []
...
```

### Create did-mount function

Next, let's add our code to a *did-mount* function called `home-did-mount`.

```clojure
(ns bootstrap-datepicker.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:input#example1 {:type "text" :placeholder "click to show datepicker"}]
   ])

;; ATTENTION \/
(defn home-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"})))))
;; ATTENTION /\
```

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns bootstrap-datepicker.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

```clojure
(ns bootstrap-datepicker.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:input#example1 {:type "text" :placeholder "click to show datepicker"}]
   ])

(defn home-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"})))))

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
