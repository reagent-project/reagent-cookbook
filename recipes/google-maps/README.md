# Problem

You want to add [Google Maps](https://developers.google.com/maps/documentation/javascript/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to follow this [example](https://developers.google.com/maps/documentation/javascript/tutorial#HelloWorld).

*Steps*

1. Create a new project
2. Obtain a Google Maps API Key
3. Add necessary items to `resources/public/index.html`
4. Create a function called `home-render` with an empty div
5. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
6. Use `home-render` and `home-did-mount` to create a reagent component called `home`
7. Add externs

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
    <div id="app"></div>

    <!-- ATTENTION \/ -->
    <!-- Google Maps -->
    <script src="https://maps.googleapis.com/maps/api/js?key=API_KEY"></script>
    <!-- Replace "API_KEY" with your actual key! -->
    <!-- ATTENTION /\ -->

    <script src="js/compiled/app.js"></script>
    <script>google_maps.core.main();</script>
  </body>
</html>
```

#### Step 4: Create a function called `home-render` with an empty div

Navigate to `src/cljs/google_maps/core.cljs`.

```clojure
(defn home-render []
  [:div {:style {:height "300px"}} 
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
(defn home-did-mount [this]
  (let [map-canvas (reagent/dom-node this)
        map-options (clj->js {"center" (js/google.maps.LatLng. -34.397, 150.644)
                              "zoom" 8})]
    (js/google.maps.Map. map-canvas map-options)))
```

#### Step 6: Use `home-render` and `home-did-mount` to create a reagent component called `home`

```clojure
(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))
```

#### Step 7: Add externs

For [advanced compilation](https://clojurescript.org/reference/advanced-compilation), we need to protect `google.maps.Map` and `google.maps.LatLng` from getting renamed. Add a dependency on the [cljsjs google-maps package](https://github.com/cljsjs/packages/tree/master/google-maps), which provides externs for the Google Maps API:

```clojure
:dependencies [cljsjs/google-maps "3.18-1"]
```

Alternatively [provide externs](https://clojurescript.org/reference/advanced-compilation#providing-externs) by creating an `externs.js` file:

```js
google.maps = {};
google.maps.Map = function(){};
google.maps.LatLng = function() {};
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
