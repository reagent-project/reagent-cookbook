# Problem

You want to add a [droppable](http://jqueryui.com/droppable/) element in your [reagent](https://github.com/holmsand/reagent) webapp.

# Solution

We are going to follow this [example](http://jqueryui.com/droppable/).

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed droppable
```

## Add jQuery ui files to index.html

Add the draggable jQuery ui files to your `resources/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">
    <title>data-tables</title>
  </head>
  <body class="container">

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<!-- ATTENTION \/ -->
    <!-- jQuery Draggable -->
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


## Familiarize yourself with directory layout

Now, let's briefly take a look at the directory layout of our reagent webapp.

```
dev/
    user.clj                --> functions to start server and browser repl (brepl)
    user.cljs               --> enabling printing to browser's console when connected through a brepl

project.clj                 --> application summary and setup

resources/
    index.html              --> this is the html for your application
    public/                 --> this is where assets for your application will be stored

src/example/
    core.cljs               ---> main reagent component for application
    css/
        screen.clj          ---> main css file using Garden
    routes.cljs             ---> defining routes using Secretary
    session.cljs            ---> contains atom with application state
    views/
        about_page.cljs     ---> reagent component for the about page
    	common.cljs         ---> common reagent components to all page views
    	home_page.cljs      ---> reagent component for the home page
    	pages.cljs          ---> map of page names to their react/reagent components
```

We can see that there are two views:

* about_page.cljs
* home_page.cljs

## Adding draggable element to home-page component

I think we should add a droppable element to the home page, but first, let's take a look at what is already there.

```clojure
(ns droppable.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   ])
```

To add a droppable element, we need the following:

* a draggable element
    * parent div with a unique id and a class of "ui-widget-content".
    * nested element inside div to be dragged
* an element that is a drop area
    * parent div with a unique id and a class of ui-widget-header
	* nested element inside div

Also, let's remove some of the boilerplate from the reagent-seed template.

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

## Converting javascript function to clojurescript

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

### Using react/reagent component lifecycle

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run to make the element droppable.  What we need to do is tap into the react/reagent component lifecycle. First, let's change `home-page` to `home-render`.


```clojure
...
(defn home-render []
...
```

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

To make the `home-page` component, which will use both the `home-render` and `home-did-mount` functions, we have to add *reagent* to our namespace.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Next, let's create our `home-page` component.

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

We keep track of our application state in a reagent atom called `app-state` located in `src/droppable/session.clj`.

```clojure
(ns droppable.session
  (:require [reagent.core :as reagent :refer [atom]]))

;; ----------
;; State
(def app-state (atom {}))

;; ----------
;; Helper Functions
(defn get-state [k & [default]]
  (clojure.core/get @app-state k default))

(defn put! [k v]
  (swap! app-state assoc k v))
```

As you can see, there are two helper functions `put!` and `get-state`. Let's refer them in our home page.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]
            [droppable.session :as session :refer [put! get-state]]))
...
```

We can initialize the number of drops to be zero by putting a key value pair into our app-state atom.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]
            [droppable.session :as session :refer [put! get-state]]))

;;; ATTENTION \/
(put! :drops 0)
;; ATTENTION /\

...
```

Next, we can get the number of `:drops` by using the `get-state` function.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]
            [droppable.session :as session :refer [put! get-state]]))

(put! :drops 0)

(defn home-render []
  [:div
   [:h2 "Home Page"]

;; ATTENTION \/
   [:div "The total number of drops has been: " [:span#total-drops (get-state :drops) ]]
;; ATTENTION /\

   [:div#draggable.ui-widget-content [:p "Drag me to my target"]]
   [:div#droppable.ui-widget-header [:p "Drop here"]]
   ])
...
```

Finally, each time the draggable element is placed on the droppable area, we can increment `:drops`.

```clojure
(ns droppable.views.home-page
  (:require [reagent.core :as reagent]
            [droppable.session :as session :refer [put! get-state]]))

(put! :drops 0)

(defn home-render []
  [:div
   [:h2 "Home Page"]

   [:div "The total number of drops has been: " [:span#total-drops (get-state :drops) ]]
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
                                   (put! :drops (inc (get-state :drops)))
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
