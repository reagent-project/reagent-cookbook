# Problem

You want to add a [draggable](http://jqueryui.com/draggable/) element in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to follow this [example](http://jqueryui.com/draggable/).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add jQuery UI files to index.html
* Add draggable element to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.
* Add CSS

Affected files:

* `resources/public/index.html`
* `src/draggable/views/home_page.cljs`
* `src/draggable/css/screen.cljs`

## Create a reagent project

```
$ lein new reagent-seed draggable
```

## Add jQuery ui files to index.html

Add the draggable jQuery ui files to your `resources/public/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>draggable</title>
  </head>
  <body>

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<!-- ATTENTION \/ -->
    <!-- jQuery Draggable -->
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<!-- ATTENTION /\ -->

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- Font Awesome -->
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <!-- CSS -->
    <link rel="stylesheet" href="css/screen.css">
    <!-- Clojurescript -->
    <script src="/js/app.js"></script>

  </body>
</html>
```

## Add draggable element to home-page component

Navigate to `src/draggable/views/home_page.cljs`.  To add a draggable element, we need the following:

* parent div with a unique id and a class of "ui-widget-content".
* nested element inside div to be dragged

```clojure
(ns draggable.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
;; ATTENTION \/
   [:div#draggable.ui-widget-content [:p "Drag me around"]]
;; ATTENTION /\
   ])
```

## Convert javascript to clojurescript

This is the javascript we want to include:

```javascript
  $(function() {
    $( "#draggable" ).draggable();
  });
```

Let's convert this to clojurescript.

```clojure
(js/$ (fn []
        (.draggable (js/$ "#draggable"))
	))
```

### Change home-page component to home-render function

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run.  What we need to do is tap into the react/reagent component lifecycle. First, let's change the `home-page` component to `home-render` function.

```clojure
...
(defn home-render []
...
```

### Create did-mount function

Next, let's add our code to a *did-mount* function.

```clojure
(ns draggable.views.home-page)

(defn home-redner []
  [:div
   [:h2 "Home Page"]
   [:div#draggable.ui-widget-content [:p "Drag me around"]]
   ])

;; ATTENTION \/
(defn home-did-mount []
  (js/$ (fn []
          (.draggable (js/$ "#draggable"))
          )))
;; ATTENTION /\
```

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns draggable.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

```clojure
(ns draggable.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#draggable.ui-widget-content [:p "Drag me around"]]
   ])

(defn home-did-mount []
  (js/$ (fn []
          (.draggable (js/$ "#draggable"))
          )))

;; ATTENTION \/
(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
;;ATTENTION /\
```

## Add CSS

The css we want to add looks like this:

```css
#draggable { width: 150px; height: 150px; padding: 0.5em; }
```

However, we want to write this in clojure using Garden instead.  We can do this by going to the `src/draggable/css/screen.clj` file and updating it as follows:

```clojure
(ns draggable.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]
;; ATTENTION \/
  ;; draggable element
  [:#draggable {:width "150px"
                :height "150px"
                :padding (em 0.5)}]
;; ATTENTION /\
  )
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
