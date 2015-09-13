# Problem

You want to use [morris](http://morrisjs.github.io/morris.js/) charts to display data in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to create a donut chart and follow this [example](http://jsbin.com/ukaxod/144/embed?html,js,output).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add div with unique id to `home-render`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home-render` and `home-did-mount` to create a reagent component called `home`
6. Add externs

#### Step 1: Create a new project

```
$ lein new rc morris
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>

    <!-- ATTENTION \/ -->
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Morris.js -->
    <script src="http://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
    <script src="http://cdn.oesmith.co.uk/morris-0.4.1.min.js"></script>
    <!-- ATTENTION /\ -->

    <script src="js/compiled/app.js"></script>
    <script>morris.core.main();</script>
  </body>
</html>
```

#### Step 3: Add div with unique id to `home-render`

Navigate to `src/cljs/morris/core.cljs`.

```clojure
(defn home-render []
  [:div#donut-example ])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
Morris.Donut({
  element: 'donut-example',
  data: [
    {label: "Download Sales", value: 12},
    {label: "In-Store Sales", value: 30},
    {label: "Mail-Order Sales", value: 20}
  ]
});
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (.Donut js/Morris (clj->js {:element "donut-example"
                              :data [{:label "Download Sales" :value 12}
                                     {:label "In-Store Sales" :value 30}
                                     {:label "Mail-Order Sales" :value 20}]})))
```

#### Step 5: Use `home-render` and `home-did-mount` to create a reagent component called `home`

```clojure
(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))
```

#### Step 6: Add externs

For advanced compilation, we need to protect `Morris.donut` from getting renamed. Add an `externs.js` file.

```js
var Morris = function(){};
Morris.Donut = function(){};
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
