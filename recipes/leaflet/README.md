# Problem

You want to add [leaflet](http://leafletjs.com/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to follow the first part of this [example](http://leafletjs.com/examples/quick-start.html).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create div with a unique id in `home-render`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home-render` and `home-did-mount` to create a reagent component called `home`
6. Add externs

#### Step 1: Create a new project

```
$ lein new rc leaflet
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>

    <!-- ATTENTION \/ -->
    <!-- Leaflet -->
    <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
    <script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
    <!-- ATTENTION /\ -->

    <script src="js/compiled/app.js"></script>
    <script>leaflet.core.main();</script>
  </body>
</html>
```

#### Step 3: Create div with a unique id in `home-render`

Navigate to `src/cljs/leaflet/core.cljs`.

```clojure
(defn home-render []
  [:div#map {:style {:height "360px"}}])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

To center a map around London, this is the javascript we need.

```javascript
var map = L.map('map').setView([51.505, -0.09], 13);

L.tileLayer('http://{s}.tiles.mapbox.com/v3/MapID/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy; [...]',
    maxZoom: 18
}).addTo(map);
```

Let's convert this to clojurescript and place in `home-did-mount`.  You will need to sign up with [Mapbox](https://www.mapbox.com/), get a [mapID](https://www.mapbox.com/help/define-map-id/) and replace the `FIXME` below with your mapID.  *Note: the mapID is different from the API key*

```clojure
(defn home-did-mount []
  (let [map (.setView (.map js/L "map") #js [51.505 -0.09] 13)]

    ;; NEED TO REPLACE FIXME with your mapID!
    (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/FIXME/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18}))
            map)))
```

#### Step 5: Use `home-render` and `home-did-mount` to create a reagent component called `home`

```clojure
(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))
```

#### Step 6: Add externs

For advanced compilation, we need to protect `L.map.setView` and `L.tileLayer.addTo` from getting renamed. Add an `externs.js` file.

```js
var L = {
    "map": {
	"setView": function(){}
    },
    "tileLayer": {
	"addTo": function(){}
    }
};
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
