# Problem

You want to add a [draggable](http://jqueryui.com/draggable/) element in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to follow this [example](http://jqueryui.com/draggable/).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create div that will contain the draggable element in `home-render`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home-render` and `home-did-mount` to create a reagent component called `home`

#### Step 1: Create a new project

```
$ lein new rc draggable
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
    <script>draggable.core.main();</script>
  </body>
</html>
```

#### Step 3: Create div that will contain the draggable element in `home-render`

Navigate to `src/cljs/draggable/core.cljs`. To add a draggable element, we need the following:

* parent div with a class of "ui-widget-content".
* nested element inside div to be dragged

```clojure
(defn home-render []
  [:div.ui-widget-content {:style {:width "150px" 
                                   :height "150px" 
                                   :padding "0.5em"}}
   [:p "Drag me around"]])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
$(function() {
  $( "#draggable" ).draggable();
});
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (js/$ (fn []
          (.draggable (js/$ "#draggable")))))
```

Rather than using jQuery to select the element by id, we can get the DOM node directly using React/Reagent.  Let's refactor the code as follows:

```clojure
(defn home-did-mount [this]
  (.DataTable (js/$ (reagent/dom-node this))))
```


#### Step 5: Use `home-render` and `home-did-mount` to create a reagent component called `home`

```clojure
(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
