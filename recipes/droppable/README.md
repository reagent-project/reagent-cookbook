# Problem

You want to add a [droppable](http://jqueryui.com/droppable/) element in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to follow this [example](http://jqueryui.com/droppable/).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add jQuery UI files to index.html
* Add droppable element to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.
* Add state to track number of drops in drop area
    * Review where application state is stored
	* Refer `global-state` and `global-put!` in home page namespace
	* Initialize number of drops in app-state atom
	* Create element to get number of drops
	* Increment number of drops when draggable element is placed on droppable area
* Add CSS

Affected files:

* `resources/public/index.html`
* `src/droppable/views/home_page.cljs`
* `src/droppable/css/screen.cljs`

Prerequisite Recipes:

* [draggable](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/draggable)

## Create a reagent project

```
$ lein new reagent-seed droppable
```

## Add jQuery ui files to index.html

Add the droppable jQuery ui files to your `resources/public/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>droppable</title>
  </head>
  <body>

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<!-- ATTENTION \/ -->
    <!-- jQuery Droppable -->
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<!-- ATTENTION /\ -->

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- Font Awesome -->
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <!-- CSS -->
    <link rel="stylesheet" href="css/screen.css">
    <!-- Clojurescript -->
    <script src="/js/app.js"></script>

  </body>
</html>
```

## Add droppable element to home-page component

Navigate to `src/droppable/views/home_page.cljs`.  To add a droppable element, we need the following:

* a draggable element
    * parent div with a unique id and a class of "ui-widget-content".
    * nested element inside div to be dragged
* an element that is a drop area
    * parent div with a unique id and a class of ui-widget-header
	* nested element inside div

```clojure
(ns droppable.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
;; ATTENTION \/
   [:div#draggable.ui-widget-content [:p "Drag me to my target"]]
   [:div#droppable.ui-widget-header [:p "Drop here"]]
;; ATTENTION /\
   ])
```

## Convert javascript to clojurescript

This is the javascript we want to include:

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

Let's convert this to clojurescript.

```clojure
(js/$ (fn []
        (.draggable (js/$ "#draggable"))
        (.droppable (js/$ "#droppable")
                    #js {:drop (fn [event ui]
                                 (this-as this
                                          (.html (.find (.addClass (js/$ this) "ui-state-highlight") 
                                                        "p")
                                                 "Dropped!"))
                                 )})))
```

### Change home-page component to home-render function

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run.  What we need to do is tap into the react/reagent component lifecycle. First, let's change the `home-page` component to `home-render` function.

```clojure
...
(defn home-render []
...
```

### Create did-mount function

Next, let's add our code to a *did-mount* function.

```clojure
(ns droppable.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#draggable.ui-widget-content [:p "Drag me to my target"]]
   [:div#droppable.ui-widget-header [:p "Drop here"]]
   ])

;; ATTENTION \/
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
;; ATTENTION /\
```

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#draggable.ui-widget-content [:p "Drag me to my target"]]
   [:div#droppable.ui-widget-header [:p "Drop here"]]
   ])

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

;; ATTENTION \/
(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
;; ATTENTION /\
```

## Add state to track number of drops in drop area

### Review where application state is stored

We keep track of our application state in a reagent atom called `app-state` located in `src/droppable/session.clj`.

```clojure
(ns droppable.session
  (:require [reagent.core :as reagent :refer [atom]]))

;; ----------
;; State
(def app-state (atom {}))

;; ----------
;; Helper Functions
(defn global-state [k & [default]]
  (get @app-state k default))

(defn global-put! [k v]
  (swap! app-state assoc k v))

(defn local-put! [a k v]
  (swap! a assoc k v))
```

### Refer global-state and global-put! in home page namespace

As you can see, there are few helper functions.  `global-state` helps you get application state from the `app-state` atom.  `global-put!` helps you change the application state of the `app-state` atom. Let's refer `global-state` and `global-put!` in our home page namespace.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]
            [droppable.session :as session :refer [global-put! global-state]]))
...
```

### Initialize number of drops in app-state atom

We can initialize the number of drops to be zero by putting a key value pair into our app-state atom.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]
            [droppable.session :as session :refer [global-put! global-state]]))

;;; ATTENTION \/
(global-put! :drops 0)
;; ATTENTION /\

...
```

### Create element to get number of drops

Next, we can get the number of `:drops` by using the `global-state` function.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]
            [droppable.session :as session :refer [global-put! global-state]]))

(global-put! :drops 0)

(defn home-render []
  [:div
   [:h2 "Home Page"]

;; ATTENTION \/
   [:div "The total number of drops has been: " [:span#total-drops (global-state :drops) ]]
;; ATTENTION /\

   [:div#draggable.ui-widget-content [:p "Drag me to my target"]]
   [:div#droppable.ui-widget-header [:p "Drop here"]]
   ])
...
```

### Increment number of drops when draggable element is placed on droppable area

Finally, each time the draggable element is placed on the droppable area, we can increment `:drops`.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]
            [droppable.session :as session :refer [global-put! global-state]]))

(global-put! :drops 0)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div "The total number of drops has been: " [:span#total-drops (global-state :drops) ]]
   [:div#draggable.ui-widget-content [:p "Drag me to my target"]]
   [:div#droppable.ui-widget-header [:p "Drop here"]]
   ])

(defn home-did-mount []
  (js/$ (fn []
          (.draggable (js/$ "#draggable"))
          (.droppable (js/$ "#droppable")
                      #js {:drop (fn [event ui]
                                   (this-as this
                                            (.html (.find (.addClass (js/$ this) "ui-state-highlight") 
                                                          "p")
                                                   "Dropped!"))
;; ATTENTION \/
                                   (global-put! :drops (inc (global-state :drops)))
;; ATTENTION /\
                                   )}))))

(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))

```

## Add CSS

The css we want to add looks like this:

```css
  #draggable { width: 100px; height: 100px; padding: 0.5em; float: left; margin: 10px 10px 10px 0; }
  #droppable { width: 150px; height: 150px; padding: 0.5em; float: left; margin: 10px; }
  #total-drops { font-size: 1.5em; color: red; }
```

However, we want to write this in clojure using Garden instead.  We can do this by going to the `src/droppable/css/screen.clj` file and updating it as follows:

```clojure
(ns droppable.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

;; ATTENTION \/
  [:#draggable {:width "100px" :height "100px" :padding (em 0.5) :float "left" :margin "10px 10px 10px 0"}]
  [:#droppable {:width "150px" :height "150px" :padding (em 0.5) :float "left" :margin "10px"}]
  [:#total-drops {:font-size (em 1.5) :color "red"}]
;; ATTENTION /\
  )
```

# Usage

To view our app, we need to perform the following steps:

Create a css file.

```
$ lein garden once
```

*Note: if it says "Successful", but you aren't able to type anything into the terminal, hit `Ctrl-c Ctrl-c`.*

Create a javascript file from your clojurescript files.

```
$ lein cljsbuild once
```

Start a repl and then start the server.

```
$ lein repl

user=> (run!)
```

Open a browser and go to *localhost:8080*. You should see your reagent application!
