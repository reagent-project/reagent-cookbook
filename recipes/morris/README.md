# Problem

You want to use [morris](http://morrisjs.github.io/morris.js/) charts to display data in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to create a donut chart and follow this [example](http://jsbin.com/ukaxod/144/embed?html,js,output).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add morris files to index.html
* Add morris to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.

Affected files:

* `resources/public/index.html`
* `src/morris/views/home_page.cljs`

## Create a reagent project

```
$ lein new reagent-seed morris
```

## Add morris files to index.html

Add morris to your `resources/public/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>morris</title>
  </head>
  <body>

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

    <!-- Morris.js -->
    <script src="http://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
    <script src="http://cdn.oesmith.co.uk/morris-0.4.1.min.js"></script>

    <!-- CSS -->
    <link rel="stylesheet" href="css/screen.css">
    <!-- Clojurescript -->
    <script src="/js/app.js"></script>

  </body>
</html>
```

## Add morris to home-page component

Navigate to `src/morris/views/home_page.cljs`. To add morris, we need to add a parent div with a unique id.

```clojure
(ns morris.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
;; ATTENTION \/
   [:div#donut-example ]
;; ATTENTION /\
   ])
```

## Convert javascript to clojurescript

This is the javascript we want to include:

```javascript
Morris.Donut({
  element: 'donut-example',
  data: [
    {label: "Download Sales", value: 12},
    {label: "In-Store Sales", value: 30},
    {label: "Mail-Order Sales", value: 20}
  ]
});
```

Let's convert this to clojurescript.

```clojure
(.Donut js/Morris (clj->js {:element "donut-example"
                            :data [{:label "Download Sales" :value 12}
                                   {:label "In-Store Sales" :value 30}
                                   {:label "Mail-Order Sales" :value 20}]}))
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
(ns morris.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#donut-example ]
   ])

;; ATTENTION \/
(defn home-did-mount []
  (.Donut js/Morris (clj->js {:element "donut-example"
                              :data [{:label "Download Sales" :value 12}
                                     {:label "In-Store Sales" :value 30}
                                     {:label "Mail-Order Sales" :value 20}]})))
;; ATTENTION /\
```

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns morris.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

```clojure
(ns morris.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#donut-example ]
   ])

(defn home-did-mount []
  (.Donut js/Morris (clj->js {:element "donut-example"
                              :data [{:label "Download Sales" :value 12}
                                     {:label "In-Store Sales" :value 30}
                                     {:label "Mail-Order Sales" :value 20}]})))

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
