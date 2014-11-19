# Problem

You want to add a [bootstrap modal](http://getbootstrap.com/javascript/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to use the [reagent-modals](https://github.com/Frozenlock/reagent-modals) library.

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed modals
```

## Add reagent-modals library

First, let's add the [reagent-modals](https://github.com/Frozenlock/reagent-modals) library to our `project.clj` file.

```clojure
(defproject modals "0.1.0-SNAPSHOT"
  :source-paths ["src" "dev"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2342"]
                 [ring "1.2.2"]
                 [compojure "1.1.6"]
                 [enlive "1.1.5"]
                 ;; ReactJS wrapper
                 [reagent "0.4.2"]
                 ;; Client-side routing
                 [secretary "1.2.0"]
                 ;; CSS
                 [garden "1.2.1"] 

;; ATTENTION \/
                 ;; modals
                 [org.clojars.frozenlock/reagent-modals "0.1.0"] ]
;; ATTENTION /\

...
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

## Adding modals to home-page component

I think we should add a bootstrap modal to the home page, but first, let's take a look at what is already there.

```clojure
(ns modals.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   ])
```

To add a bootstrap modal, we need to require the reagent-modals library in our namespace.

```clojure
(ns modals.views.home-page
  (:require [reagent-modals.modals :as reagent-modals]))

...
```

Next, we create a modal function called `modal-example`.

```clojure
(ns modals.views.home-page
  (:require [reagent-modals.modals :as reagent-modals]))

;; ATTENTION \/
(defn modal-example []
  (reagent-modals/modal [:div "some message to the user!"]))
;; ATTENTION /\

...
```

Finally, we 1) include the `modal-window` function, and 2) execute the `modal-example` function when we click on a button.

```clojure
(ns modals.views.home-page
  (:require [reagent-modals.modals :as reagent-modals]))

(defn modal-example []
  (reagent-modals/modal [:div "some message to the user!"]))

;; ATTENTION \/
(defn home-page []
  [:div
   [:h2 "Home Page"]
   [reagent-modals/modal-window]
   [:div.btn.btn-primary {:on-click modal-example} "My Modal"]
   ])
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
