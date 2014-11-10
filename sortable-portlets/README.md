# Problem

You want to add [sortable portlets](http://jqueryui.com/sortable/#portlets) to your [reagent](https://github.com/holmsand/reagent) webapp.

# Solution

We are going to follow this [example](http://jqueryui.com/sortable/#portlets).

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

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
    <title>data-tables</title>
  </head>
  <body class="container">

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<!-- ATTENTION \/ -->
    <!-- jQuery ui -->
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

## Adding sortable portlets to home-page component

I think we should add sortable portlets to the home page, but first, let's take a look at what is already there.

```clojure
(ns sortable-portlets.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   ])
```

Let's create our portlets by adding some divs.  We need to add a few classes to our divs so jQuery can manipulate them and add the portlet functionality. Also, let's remove some of the boilerplate from the reagent-seed template.

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

## Converting javascript to clojurescript

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

### Using react/reagent component lifecycle

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run to make the portlet sortable.  What we need to do is tap into the react/reagent component lifecycle. First, let's change `home-page` to `home-render`.

```clojure
...
(defn home-render []
...
```

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

To make the `home-page` component, which will use both the `home-render` and `home-did-mount` functions, we have to add *reagent* to our namespace.

```clojure
(ns sortable-portlets.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component.

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
(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

;; ATTENTION \/
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
