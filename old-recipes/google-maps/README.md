# Problem

You want to add [Google Maps](https://developers.google.com/maps/documentation/javascript/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to follow this [example](https://developers.google.com/maps/documentation/javascript/tutorial#HelloWorld).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Obtain a Google Maps API Key
* Add Google Maps script to index.html
* Add Google Maps element to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.
* Add CSS

Affected files:

* `resources/public/index.html`
* `src/google-maps/views/home_page.cljs`
* `src/google-maps/css/screen.cljs`

## Create a reagent project

```
$ lein new reagent-seed google-maps
```

## Obtain a Google Maps API Key

In order to use the Google Maps API, we must first obtain an API Key.  Follow the steps [here](https://developers.google.com/maps/documentation/javascript/tutorial#api_key), then copy the API key.
  
## Add Google Maps script to index.html

Add the Google Maps file to your `resources/index.html` file.  Make sure to replace API_KEY with the key provided by Google.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>google-maps</title>
  </head>
  <body>

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<!-- ATTENTION \/ -->
    <!-- Google Maps -->
     <script src="https://maps.googleapis.com/maps/api/js?key=API_KEY"></script>
     <!-- Replace "API_KEY" with your actual key! -->
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

## Add Google Maps to home-page component

Navigate to `src/google_maps/views/home_page.cljs`.  To add a map, we need to add a div with a unique id.

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

## Convert javascript function to clojurescript

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

### Change home-page component to home-render function

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run.  What we need to do is tap into the react/reagent component lifecycle. First, let's change the `home-page` component to `home-render` function.

```clojure
...
(defn home-render []
...
```

### Create did-mount function

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
                              "zoom"   8})]
    (js/google.maps.Map. map-canvas map-options)))
;; ATTENTION /\
```

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns google-maps.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

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
                              "zoom"   8})]
    (js/google.maps.Map. map-canvas map-options)))

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

However, we want to write this in clojure using Garden instead.  We can do this by going to the `src/google_maps/css/screen.clj` file and updating it as follows:

```clojure
(ns google-maps.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

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

In `resources/public/index.html`, replace `API_KEY` with your actual API key. If you don't have one, follow the steps [here](https://developers.google.com/maps/documentation/javascript/tutorial#api_key).

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
