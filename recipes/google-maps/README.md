# Problem

You want to add [Google Maps](https://developers.google.com/maps/documentation/javascript/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-google-maps2.s3-website-us-west-1.amazonaws.com/)

We are going to follow this [example](https://developers.google.com/maps/documentation/javascript/tutorial#HelloWorld).

*Steps*

1. Create a new project
2. Obtain a Google Maps API Key
3. Add necessary items to `resources/public/index.html`
4. Create div with a unique id in `home`
5. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
6. Use `home` and `home-did-mount` to create a reagent component called `home-component`
7. Change the initially rendered component from `home` to `home-componen`
8. Create `resources/public/css/screen.css` files and add necessary CSS
9. Add CSS file to `resources/public/index.html`

#### Step 1: Create a new project

```
$ lein new rc google-maps
```

#### Step 2: Obtain a Google Maps API Key

In order to use the Google Maps API, we must first obtain an API Key.  Follow the steps [here](https://developers.google.com/maps/documentation/javascript/tutorial#api_key), then copy the API key.

#### Step 3: Add necessary items to `resources/public/index.html`

Add the script tag below, but replace `API_KEY` with you *actual* API key.

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
<!-- ATTENTION \/ -->
    <!-- Google Maps -->
     <script src="https://maps.googleapis.com/maps/api/js?key=API_KEY"></script>
     <!-- Replace "API_KEY" with your actual key! -->
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 4: Create div with a unique id in `home`

Navigate to `src/cljs/google_maps/core.cljs`.

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
;; ATTENTION \/
   [:div#map-canvas]
;; ATTENTION /\
   ])
```

#### Step 5: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

To center a map around Sydney, this is the javascript we need.

```javascript
    var mapOptions = {
        center: { lat: -34.397, lng: 150.644},
        zoom: 8
    };

    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (let [map-canvas  (.getElementById js/document "map-canvas")
        map-options (clj->js {"center" (google.maps.LatLng. -34.397, 150.644)
                              "zoom" 8})]
        (js/google.maps.Map. map-canvas map-options)))
```

#### Step 6: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))
```

#### Step 7: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
```

#### Step 8: Create `resources/public/css/screen.css` files and add necessary CSS

```css
#map-canvas { height: 300px; }
```

#### Step 9:  Add CSS file to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <!-- Google Maps -->
     <script src="https://maps.googleapis.com/maps/api/js?key=API_KEY"></script>
     <!-- Replace "API_KEY" with your actual key! -->
<!-- ATTENTION \/ -->
    <!-- CSS -->
    <link rel="stylesheet" href="/css/screen.css">
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
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
