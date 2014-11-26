# Problem

You want to add [leaflet](http://leafletjs.com/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to follow the first part of this [example](http://leafletjs.com/examples/quick-start.html).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add Leaflet files to index.html
* Add Leaflet element to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
        * Obtain a MapID key
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.
* Add CSS

Affected files:

* `resources/public/index.html`
* `src/leaflet/views/home_page.cljs`
* `src/leaflet/css/screen.cljs`

## Create a reagent project

```
$ lein new reagent-seed leaflet
```

## Add leaflet files to index.html

Add the leaflet files to your `resources/public/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>leaflet</title>
  </head>
  <body>

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<!-- ATTENTION \/ -->
    <!-- Leaflet -->
     <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
     <script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
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

## Add Leaflet to home-page component

Navigate to `src/leaflet/views/home_page.cljs`. To add a leaflet, we need to add a div with a unique id. Also, let's remove some of the boilerplate from the reagent-seed template.

```clojure
(ns leaflet.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
;; ATTENTION \/
   [:div#map ]
;; ATTENTION /\
   ])
```

## Convert javascript to clojurescript

To center a map around London, this is the javascript we want to include:

```javascript
var map = L.map('map').setView([51.505, -0.09], 13);

L.tileLayer('http://{s}.tiles.mapbox.com/v3/MapID/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy; [...]',
    maxZoom: 18
}).addTo(map);
```

Let's convert this to clojurescript.

```clojure
(let [map (.setView (.map js/L "map") #js [51.505 -0.09] 13)]

  ;; NEED TO REPLACE FIXME with your mapID!
  (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/FIXME/{z}/{x}/{y}.png"
                      (clj->js {:attribution "Map data &copy; [...]"
                                :maxZoom 18}))
          map))
```

### Change home-page component to home-render function

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run.  What we need to do is tap into the react/reagent component lifecycle. First, let's change the `home-page` component to `home-render` function.

```clojure
...
(defn home-render []
...
```

### Create did-mount function

Next, let's add our code to a *did-mount* component.

*(Note:  You need to sign up with [Mapbox](https://www.mapbox.com/), get a [mapID](https://www.mapbox.com/help/define-map-id/) and replace the FIXME below with your mapID)*

```clojure
(ns leaflet.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#map ]
   ])

;; ATTENTION \/
(defn home-did-mount []
  (let [map (.setView (.map js/L "map") #js [51.505 -0.09] 13)]
    
    ;;; NEED TO REPLACE FIXME with your mapID!
    (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/FIXME/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18}))
            map)))
;; ATTENTION /\
```

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns leaflet.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

```clojure
(ns leaflet.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#map ]
   ])

(defn home-did-mount []
  (let [map (.setView (.map js/L "map") #js [51.505 -0.09] 13)]
    
    ;;; NEED TO REPLACE FIXME with your mapID!
    (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/FIXME/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18}))
            map)))

;; ATTENTION \/
(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
;; ATTENTION /\
```

## Add CSS

The css we want to add looks like this:

```css
#map { height: 180px; }
```

However, we want to write this in clojure using Garden instead.  We can do this by going to the `src/leaflet/css/screen.clj` file and updating it as follows:

```clojure
(ns leaflet.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]
;; ATTENTION \/
  ;; Leafleat
  [:#map {:height "180px"}]
;; ATTENTION /\
  )
```

# Usage

To view our app, we need to perform the following steps:

Navigate to `src/leaflet/views/home_page.clj` and replace the FIXME with your [mapID](https://www.mapbox.com/help/define-map-id/).

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
