# Problem

You want to add [leaflet](http://leafletjs.com/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-leaflet2.s3-website-us-west-1.amazonaws.com/)

We are going to follow the first part of this [example](http://leafletjs.com/examples/quick-start.html).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create div with a unique id in `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-componen`
7. Create `resources/public/css/screen.css` files and add necessary CSS
8. Add CSS file to `resources/public/index.html`

#### Step 1: Create a new project

```
$ lein new rc leaflet
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
<!-- ATTENTION \/ -->
    <!-- Leaflet -->
     <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
     <script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Create div with a unique id in `home`

Navigate to `src/cljs/leaflet/core.cljs`.

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
;; ATTENTION \/
   [:div#map ]
;; ATTENTION /\
   ])
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

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))
```

#### Step 6: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
```

#### Step 7: Create `resources/public/css/screen.css` files and add necessary CSS

```css
#map { height: 360px; }
```

#### Step 8:  Add CSS file to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <!-- Leaflet -->
     <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
     <script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
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
