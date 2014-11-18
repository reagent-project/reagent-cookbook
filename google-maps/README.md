# Problem

You want to add [Google Maps](https://developers.google.com/maps/documentation/javascript/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to follow this [example](https://developers.google.com/maps/documentation/javascript/tutorial#HelloWorld).

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed google-maps
```

## Obtaining a Google Maps API Key

In order to use the Google Maps API, we must first obtain an API Key.  Follow the steps [here](https://developers.google.com/maps/documentation/javascript/tutorial#api_key), then copy the API key.
  
## Add Google Maps file to index.html

Add the Google Maps file to your `resources/index.html` file.  Make sure to replace API_KEY with the key provided by Google.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>google-maps</title>
  </head>
  <body class="container">

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<!-- ATTENTION \/ -->
    <!-- Google Maps -->
     <script src="https://maps.googleapis.com/maps/api/js?key=API_KEY"></script>
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

## Adding Google Maps to home-page component

I think we should add Google Maps to the home page, but first, let's take a look at what is already there.

```clojure
(ns google-maps.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]])
```

To add a map, we need to add a div with a unique id. Also, let's remove some of the boilerplate from the reagent-seed template.

```clojure
(ns google-maps.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]

;; ATTENTION \/
   [:div#map-canvas]
;; ATTENTION /\

   ])
```

## Converting javascript function to clojurescript

To center a map around Sydney, this is the javascript we want to include:

```javascript
    var mapOptions = {
        center: { lat: -34.397, lng: 150.644},
        zoom: 8
    };

    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

```

Let's convert this to clojurescript.

```clojure
(let [map-canvas  (.getElementById js/document "map-canvas")
      map-options (clj->js {"center" (google.maps.LatLng. -34.397, 150.644)
                            "zoom" 8})
  (js/google.maps.Map. map-canvas map-options)])
```

### Using react/reagent component lifecycle

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run to make the map.  What we need to do is tap into the react/reagent component lifecycle. First, let's change `home-page` to `home-render`.

```clojure
...
(defn home-render []
...
```

Next, let's add our code to a *did-mount* component.

```clojure
(ns google-maps.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#map-canvas]])

;; ATTENTION \/
(defn home-did-mount []
  (let [map-canvas  (.getElementById js/document "map-canvas")
        map-options (clj->js {"center" (google.maps.LatLng. -34.397, 150.644)
                              "zoom"   8})
    (js/google.maps.Map. map-canvas map-options)])
;; ATTENTION /\
```

To make the `home-page` component, which will use both the `home-render` and `home-did-mount` functions, we have to add *reagent* to our namespace.

```clojure
(ns google-maps.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component.

```clojure
(ns google-maps.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#map-canvas]])

(defn home-did-mount []
  (let [map-canvas  (.getElementById js/document "map-canvas")
        map-options (clj->js {"center" (google.maps.LatLng. -34.397, 150.644)
                              "zoom"   8})
    (js/google.maps.Map. map-canvas map-options)])

;; ATTENTION \/
(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
;; ATTENTION /\
```

## Add CSS

The css we want to add looks like this:

```css
#map-canvas { height: 300px; }
```

However, we want to write this in clojure using Garden instead.  We can do this by going to the `src/google-maps/css/screen.clj` file and updating it as follows:

```clojure
(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

;; ATTENTION \/
  ;; Google Map
  [:#map-canvas {:height "300px"}]
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
