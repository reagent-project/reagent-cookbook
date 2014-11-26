# Problem

You want to add [sortable portlets](http://jqueryui.com/sortable/#portlets) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to follow this [example](http://jqueryui.com/sortable/#portlets).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add jQuery UI files to index.html
* Add sortable portlets elements to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.
* Add CSS

Affected files:

* `resources/public/index.html`
* `src/sortable_portlets/views/home_page.cljs`
* `src/sortable_portlets/css/screen.cljs`

## Create a reagent project

```
$ lein new reagent-seed sortable-portlets
```

## Add jQuery ui files to index.html

Add the jQuery ui files to your `resources/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>sortable-portlets</title>
  </head>
  <body>

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

    <!-- jQuery ui -->
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>

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

## Add sortable portlets to home-page component

Navigate to `src/sortable_portlets/views/home_page.cljs`.  Let's create our portlets by adding some divs.  We need to add a few classes to our divs so jQuery can manipulate them and add the portlet functionality.

```clojure
(ns sortable-portlets.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]

;; ATTENTION \/
   [:div.column
    [:div.portlet
     [:div.portlet-header "Feeds"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]]
    [:div.portlet
     [:div.portlet-header "News"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ]

   [:div.column
    [:div.portlet
     [:div.portlet-header "Shopping"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ] 

   [:div.column
    [:div.portlet
     [:div.portlet-header "Links"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]]
    [:div.portlet
     [:div.portlet-header "Images"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ]
;; ATTENTION /\
   ])
```

## Convert javascript to clojurescript

This is the javascript we want to include:

```javascript
  $(function() {
    $( ".column" ).sortable({
      connectWith: ".column",
      handle: ".portlet-header",
      cancel: ".portlet-toggle",
      placeholder: "portlet-placeholder ui-corner-all"
    });
 
    $( ".portlet" )
      .addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
      .find( ".portlet-header" )
        .addClass( "ui-widget-header ui-corner-all" )
        .prepend( "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>");
 
    $( ".portlet-toggle" ).click(function() {
      var icon = $( this );
      icon.toggleClass( "ui-icon-minusthick ui-icon-plusthick" );
      icon.closest( ".portlet" ).find( ".portlet-content" ).toggle();
    });
  });
```

Let's convert this to clojurescript.

```clojure
(js/$ (fn []
        (.sortable (js/$ ".column") (clj->js {:connectWith ".column"
                                              :handle ".portlet-header"
                                              :cancel ".portlet-toggle"
                                              :placeholder "portlet-placeholder ui-corner-all"}))

        (.. (js/$ ".portlet")
            (addClass "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
            (find ".portlet-header")
            (addClass "ui-widget-header ui-corner-all")
            (prepend "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>"))

        (.click (js/$ ".portlet-toggle") (fn []
                                           (this-as this 
                                                    (let [icon (js/$ this)]
                                                      (.toggleClass icon "ui-icon-minusthick ui-icon-plusthick")
                                                      (.toggle (.find (.closest icon ".portlet") ".portlet-content"))
                                                      ))))))
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
(ns sortable-portlets.views.home-page)

(defn home-render []
...
)

;; ATTENTION \/
(defn home-did-mount []
  (js/$ (fn []
          (.sortable (js/$ ".column") (clj->js {:connectWith ".column"
                                                :handle ".portlet-header"
                                                :cancel ".portlet-toggle"
                                                :placeholder "portlet-placeholder ui-corner-all"}))

          (.. (js/$ ".portlet")
              (addClass "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
              (find ".portlet-header")
              (addClass "ui-widget-header ui-corner-all")
              (prepend "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>"))

          (.click (js/$ ".portlet-toggle") (fn []
                                             (this-as this 
                                                      (let [icon (js/$ this)]
                                                        (.toggleClass icon "ui-icon-minusthick ui-icon-plusthick")
                                                        (.toggle (.find (.closest icon ".portlet") ".portlet-content"))
                                                        )))))))
;; ATTENTION /\
```

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns sortable-portlets.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

```clojure
(ns sortable-portlets.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]

   [:div.column
    [:div.portlet
     [:div.portlet-header "Feeds"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]]
    [:div.portlet
     [:div.portlet-header "News"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ]

   [:div.column
    [:div.portlet
     [:div.portlet-header "Shopping"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ] 

   [:div.column
    [:div.portlet
     [:div.portlet-header "Links"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]]
    [:div.portlet
     [:div.portlet-header "Images"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ]
   ])

(defn home-did-mount []
  (js/$ (fn []
          (.sortable (js/$ ".column") (clj->js {:connectWith ".column"
                                                :handle ".portlet-header"
                                                :cancel ".portlet-toggle"
                                                :placeholder "portlet-placeholder ui-corner-all"}))

          (.. (js/$ ".portlet")
              (addClass "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
              (find ".portlet-header")
              (addClass "ui-widget-header ui-corner-all")
              (prepend "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>"))

          (.click (js/$ ".portlet-toggle") (fn []
                                             (this-as this 
                                                      (let [icon (js/$ this)]
                                                        (.toggleClass icon "ui-icon-minusthick ui-icon-plusthick")
                                                        (.toggle (.find (.closest icon ".portlet") ".portlet-content"))
                                                        )))))))
														
;; ATTENTION \/
(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
;;ATTENTION /\
```

## Add CSS

The css we want to add looks like this:

```css
  body {
    min-width: 520px;
  }
  .column {
    width: 170px;
    float: left;
    padding-bottom: 100px;
  }
  .portlet {
    margin: 0 1em 1em 0;
    padding: 0.3em;
  }
  .portlet-header {
    padding: 0.2em 0.3em;
    margin-bottom: 0.5em;
    position: relative;
  }
  .portlet-toggle {
    position: absolute;
    top: 50%;
    right: 0;
    margin-top: -8px;
  }
  .portlet-content {
    padding: 0.4em;
  }
  .portlet-placeholder {
    border: 1px dotted black;
    margin: 0 1em 1em 0;
    height: 50px;
  }
```

However, we want to write this in clojure using Garden instead.  We can do this by going to the `src/sortable_portlets/css/screen.clj` file and updating it as follows:

```clojure
(ns sortable-portlets.css.screen
  (:require  [garden.def :refer [defstyles]]
             [garden.units :as u :refer [em]]
             [garden.color :as color :refer [rgb]]))

(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

;; ATTENTION \/
  ;; sortable portlets
  [:body {:min-width "520px"}]
  [:.column {:width "170px"
             :float "left"
             :padding-bottom "100px"}]
  [:.portlet {:margin "0 1em 1em 0"
              :padding "0.3em"}]
  [:.portlet-header {:padding "0.2em 0.3em"
                     :margin-bottom "0.5em"
                     :position "relative"}]
  [:.portlet-toggle {:position "absolute"
                     :top "50%"
                     :right "0"
                     :margin-top "-8px"}]
  [:.portlet-content {:padding "0.4em"}]
  [:.portlet-placeholder {:border "1px dotted black"
                          :margin "0 1em 1em 0"
                          :height "50px"}]
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
