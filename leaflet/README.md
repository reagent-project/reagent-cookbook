# Problem

You want to add [leaflet](http://leafletjs.com/) to your [reagent](https://github.com/holmsand/reagent) webapp.

# Solution

We are going to follow the first part of this [example](http://leafletjs.com/examples/quick-start.html).

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed leaflet
```

## Add leaflet files to index.html

Add the leaflet files to your `resources/index.html` file.

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

## Adding leaflet to home-page component

I think we should add leaflet to the home page, but first, let's take a look at what is already there.

```clojure
(ns leaflet.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   ])
```

To add a leaflet, we need to add a div with a unique id.

```clojure
(ns leaflet.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]

;; ATTENTION \/
   [:div#map ]
;; ATTENTION /\

   ])
```

## Converting javascript function to clojurescript

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

### Using react/reagent component lifecycle

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run to make the leaflet.  What we need to do is tap into the react/reagent component lifecycle. First, let's change `home-page` to `home-render`.

```clojure
...
(defn home-render []
...
```

Next, let's add our code to a *did-mount* component.

*(Note:  You need to sign up with [Mapbox](https://www.mapbox.com/), get a [mapID](https://www.mapbox.com/help/define-map-id/) and replace the FIXME below with your mapID)*

```clojure
(ns leaflet.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
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

To make the `home-page` component, which will use both the `home-render` and `home-did-mount` functions, we have to add *reagent* to our namespace.

```clojure
(ns leaflet.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component.

```clojure
(ns leaflet.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
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
