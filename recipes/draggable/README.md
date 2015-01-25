# Problem

You want to add a [draggable](http://jqueryui.com/draggable/) element in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-draggable2.s3-website-us-west-1.amazonaws.com/)

We are going to follow this [example](http://jqueryui.com/draggable/).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create div that will contain the draggable element in `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-componen`
7. Create `resources/public/css/screen.css` files and add necessary CSS
8. Add CSS file to `resources/public/index.html`

#### Step 1: Create a new project

```
$ lein new rc draggable
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION \/ -->
    <!-- jQuery UI -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Create div that will contain the draggable element in `home`

Navigate to `src/cljs/draggable/core.cljs`. To add a draggable element, we need the following:

* parent div with a unique id and a class of "ui-widget-content".
* nested element inside div to be dragged

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
;; ATTENTION \/
   [:div#draggable.ui-widget-content [:p "Drag me around"]]
;; ATTENTION /\
   ])
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
          (.draggable (js/$ "#draggable"))
          )))
```

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:component-function home
                         :component-did-mount home-did-mount}))
```

#### Step 6: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
```

#### Step 7: Create `resources/public/css/screen.css` files and add necessary CSS

```css
#draggable { width: 150px; height: 150px; padding: 0.5em; }
```

#### Step 8:  Add CSS file to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
    <!-- jQuery UI -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
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
	
