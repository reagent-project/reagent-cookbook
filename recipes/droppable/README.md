# Problem

You want to add a [droppable](http://jqueryui.com/droppable/) element in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-droppable2.s3-website-us-west-1.amazonaws.com/)

We are going to follow this [example](http://jqueryui.com/droppable/).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create draggable element and a drop area in `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`
7. Create `resources/public/css/screen.css` files and add necessary CSS
8. Add CSS file to `resources/public/index.html`

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

#### Step 3: Create draggable element and a drop area in `home`

Navigate to `src/cljs/droppable/core.cljs`. To add a droppable element, we need the following:

* a draggable element
    * parent div with a unique id and a class of "ui-widget-content".
    * nested element inside div to be dragged
* an element that is a drop area
    * parent div with a unique id and a class of ui-widget-header
	* nested element inside div

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
;; ATTENTION \/
   [:div#draggable.ui-widget-content [:p "Drag me to my target"]]
   [:div#droppable.ui-widget-header [:p "Drop here"]]
;; ATTENTION /\
   ])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
  $(function() {
    $( "#draggable" ).draggable();
    $( "#droppable" ).droppable({
      drop: function( event, ui ) {
        $( this )
          .addClass( "ui-state-highlight" )
          .find( "p" )
            .html( "Dropped!" );
      }
    });
  });
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (js/$ (fn []
        (.draggable (js/$ "#draggable"))
        (.droppable (js/$ "#droppable")
                    #js {:drop (fn [event ui]
                                 (this-as this
                                          (.html (.find (.addClass (js/$ this) "ui-state-highlight") 
                                                        "p")
                                                 "Dropped!"))
                                 )}))))
```

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:render home
                         :component-did-mount home-did-mount}))
```

#### Step 6: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
```

#### Step 7: Create `resources/public/css/screen.css` files and add necessary CSS

```css
#draggable { width: 100px; height: 100px; padding: 0.5em; float: left; margin: 10px 10px 10px 0; }
#droppable { width: 150px; height: 150px; padding: 0.5em; float: left; margin: 10px; }
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
	
