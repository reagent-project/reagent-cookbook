# Problem

You want to add [mo · js](https://github.com/legomushroom/mojs) animations to your [Reagent](https://github.com/reagent-project/reagent) web application.

# Solution

*Steps*

1. Create a new project
2. Add the `mo . js` library to `resources/public/vendor/js`
3. Add necessary items to `resources/public/index.html`
4. Add some css to `resources/public/css/animation.css`
5. Add `animation-did-mount` function to render a Tween using the DOM node
6. Add a `translate-y` function to update the node during animation
7. Create an `animation` component that will render a `div` element
8. Add externs in `project.clj` using the `mo . js` library

#### Step 1: Create a new project

```
$ lein new rc mojs-animation
```

#### Step 2: Add the `mo . js` library to `resources/public/vendor/js`

```
wget -O resources/public/vendor/js/mo.min.js http://cdn.jsdelivr.net/mojs/latest/mo.min.js
```

#### Step 3: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>    
    <!-- mo.js -->
     <link rel="stylesheet" href="css/animation.css">
    <script src="vendor/js/mo.min.js"></script>
    <script src="js/compiled/app.js"></script>
    <script>mojs_animation.core.main();</script>
  </body>
</html>
```

#### Step 4: Add some css to `resources/public/css/animation.css`

```css
body {
  max-width: 600px;
  margin: 0 auto;
  padding-top: 72px;
}

.square {
  width:      50px;
  height:     50px;
  background: #F64040;
  position:   absolute;
  top:        10px;
  left:       50%;
  margin-left: -25px;
  margin-top:  -25px;
}
```

#### Step 5: Add `animation-did-mount` function to render a Tween using the DOM node

```clojure
(defn animation-did-mount [this]    
  (.run
   (js/mojs.Tween.
    (clj->js
     {:repeat 999
      :delay 2000
      :onUpdate (translate-y (reagent/dom-node this))}))))
```

#### Step 6: Add a `translate-y` function to update the node during animation

```clojure
(defn translate-y [node]
  (fn [progress]
    (set! (-> node .-style .-transform)
     (str "translateY(" (* 200 progress) "px)"))))
```

#### Step 7: Create an `animation` component that will render a `div` element

The component will call the external Js code when the node is mounted in the browser DOM. This has to be done in the `component-did-mount` state.

```clojure
(defn animation []
  (reagent/create-class {:render (fn [] [:div.square])
                         :component-did-mount animation-did-mount}))

(defn ^:export main []
  (reagent/render [animation]
                  (.getElementById js/document "app")))

```

#### Step 8: Add externs

For advanced compilation, we need to protect `mojs` namespaced functions from getting renamed. We'll open `project.clj` and add a reference to the externs in the compiler portion under the  `:cljsbuild` key. The `:closure-warnings` key will suppress the warnings when parsing externs from the library.

```clojure
:externs ["resources/public/vendor/js/mo.min.js"]
:closure-warnings {:externs-validation :off
                   :non-standard-jsdoc :off}
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once
```

Open `resources/public/index.html`.
