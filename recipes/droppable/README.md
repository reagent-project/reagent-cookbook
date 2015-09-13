# Problem

You want to add a [droppable](http://jqueryui.com/droppable/) element in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to take inspiration from this [example](http://jqueryui.com/droppable/), but do it reagent-style.

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create a `draggable` component
4. Create a `drop-area` component
5. Add `draggable` and `drop-area` to `home`
6. Add externs

Prerequisite Recipes:

* [draggable](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/draggable)

#### Step 1: Create a new project

```
$ lein new rc droppable
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>

    <!-- ATTENTION \/ -->
    <script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.min.css">
    <script src="http://code.jquery.com/ui/1.11.2/jquery-ui.min.js"></script>
    <!-- ATTENTION /\ -->

    <script src="js/compiled/app.js"></script>
    <script>droppable.core.main();</script>
  </body>
</html>
```

#### Step 3: Create a `draggable` component

```clojure
(defn draggable-render []
  [:div.ui-widget-content {:style {:width "100px"
                                   :height "100px" 
                                   :padding "0.5em"
                                   :float "left" 
                                   :margin "10px 10px 10px 0"}}
   [:p "Drag me to my target"]])

(defn draggable-did-mount [this]
  (.draggable (js/$ (reagent/dom-node this))))

(defn draggable []
  (reagent/create-class {:reagent-render draggable-render
                         :component-did-mount draggable-did-mount}))
```

#### Step 4: Create a `drop-area` component

Let's create a reagent atom called `app-state` that will store the class and text of our drop-area component.  Next, let's create the render function for our drop-area component, called `drop-area-render`, that listens to changes in `app-state`.


```clojure
(def app-state (reagent/atom {:drop-area {:class "ui-widget-header"
                                          :text "Drop here"}}))
										  
(defn drop-area-render []
  (let [class (get-in @app-state [:drop-area :class])
        text (get-in @app-state [:drop-area :text])]
    [:div {:class class
           :style {:width "150px" 
                   :height "150px"
                   :padding "0.5em" 
                   :float "left" 
                   :margin "10px"}}
     [:p text]]))
```

We need to use jQuery UI's `.droppable` method on our drop-area component.  On the `drop` event, we can pass a handler that will update our `app-state` with a new class and text.

```clojure
(defn drop-area-did-mount [this]
  (.droppable (js/$ (reagent/dom-node this))
              #js {:drop (fn []
                           (swap! app-state assoc-in [:drop-area :class] "ui-widget-header ui-state-highlight")
                           (swap! app-state assoc-in [:drop-area :text] "Dropped!"))}))
```

Finally, let's create our `drop-area` component by combining `drop-area-did-mount` with `drop-area-render`.

```clojure
(defn drop-area []
  (reagent/create-class {:reagent-render drop-area-render
                         :component-did-mount drop-area-did-mount}))
```

#### Step 5: Add `draggable` and `drop-area` to `home`

```clojure
(defn home []
  [:div
   [draggable]
   [drop-area]])
```

#### Step 6: Add externs

For advanced compilation, we need to protect `$.draggable` and `$.droppable` from getting renamed. Add an `externs.js` file.

```js
var $ = function(){};
$.draggable = function(){};
$.droppable = function(){};
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

