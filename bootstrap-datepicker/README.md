# Problem

You want to use [bootstrap-datepicker](https://github.com/eternicode/bootstrap-datepicker) to display a dropdown calendar in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to follow this [example](http://runnable.com/UmOlOZbXvZRqAABU/bootstrap-datepicker-example-text-input-with-specifying-date-format2).

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed bootstrap-datepicker
```

## Add bootstrap-datepicker files to index.html

Add the bootstrap-datepicker files to your `resources/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>bootstrap-datepicker</title>
  </head>
  <body class="container">

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- Font Awesome -->
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">

<!-- ATTENTION \/ -->
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker.css">
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.js"></script>
<!-- ATTENTION /\ -->

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

## Adding bootstrap-datepicker to home-page component

I think we should add bootstrap-datepicker to the home page, but first, let's take a look at what is already there.

```clojure
(ns bootstrap-datepicker.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   ])
```

To add a bootstrap-datepicker, we need to create a text input with unique id. Also, let's remove some of the boilerplate from the reagent-seed template.

```clojure
(ns bootstrap-datepicker.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]

;; ATTENTION \/
   [:input#example1 {:type "text" :placeholder "click to show datepicker"}]
;; ATTENTION /\
   ])

## Converting javascript function to clojurescript

To add bootstrap-datepicker, this is the javascript we want to include:

```javascript
// When the document is ready
$(document).ready(function () {
    
    $('#example1').datepicker({
        format: "dd/mm/yyyy"
    });
    
});
```

Let's convert this to clojurescript.

```clojure
(.ready (js/$ js/document) 
        (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"}))))
```

### Using react/reagent component lifecycle

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run to make the bootstrap-datepicker.  What we need to do is tap into the react/reagent component lifecycle. First, let's change `home-page` to `home-render`.

```clojure
...
(defn home-render []
...
```

Next, let's add our code to a *did-mount* component.

```clojure
(ns bootstrap-datepicker.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:input#example1 {:type "text" :placeholder "click to show datepicker"}]
   ])

;; ATTENTION \/
(defn home-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"})))))
;; ATTENTION /\
```

To make the `home-page` component, which will use both the `home-render` and `home-did-mount` functions, we have to add *reagent* to our namespace.

```clojure
(ns bootstrap-datepicker.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component.

```clojure
(ns bootstrap-datepicker.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]

   [:input#example1 {:type "text" :placeholder "click to show datepicker"}]

   ])

(defn home-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"})))))

;; ATTENTION \/
(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
;; ATTENTION /\
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
