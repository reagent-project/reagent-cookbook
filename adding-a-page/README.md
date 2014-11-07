# Problem

You want to add a new page to your [reagent](https://github.com/holmsand/reagent) webapp.

# Solution

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template, since it already come with two pages.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed adding-a-page
```

## Familiarize yourself with reagent-seed template

This section goes over the organization of the reagent-seed template. If you are already familiar, please skip to the next section.

### Directory layout

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

We can see that there are two views (i.e., pages):

* about_page.cljs
* home_page.cljs

### Local State

The way that reagent-seed is set up, there is an *atom* called `app-state` (located in `src/adding_a_page/session.cljs`) that contains the local state of the webapp.

```clojure
(ns adding-a-page.session
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

As you can see, there are two helper functions to `get-state` from the atom and to `put!` state into the atom.

### Routes

Open `src/adding_a_page/routes.cljs`.  The `app-routes` function says that whenever we navigate to the route `#/`, the `session/put!` function will define the `:current-page` in our `session/app-state` atom to be `(pages/pages :home-page)`.

```clojure
(ns adding-a-page.routes
    (:require [secretary.core :as secretary :include-macros true :refer [defroute]]
              [adding-a-page.session :as session]
              [adding-a-page.views.pages :as pages]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

...

;; ----------
;; Routes
(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (session/put! :current-page (pages/pages :home-page))  ;; ATTENTION
    (session/put! :nav "home"))
...
```

### home-page (a reagent component)

So what does `(pages/pages :home-page)` evaluate to?  Let's open up the `src/adding_a_page/views/pages.cljs` file.

```clojure
(ns adding-a-page.views.pages
  (:require [adding-a-page.views.home-page :refer [home-page]]
            [adding-a-page.views.about-page :refer [about-page]]))

(def pages {:home-page home-page  ;; ATTENTION
            :about-page about-page})
```

This means that when we navigate to the route `#/`, we are setting `:current-page` key in the `session/app-state` atom to be the `home-page` function.

Ok, what is the `home-page` function? Let's go to `src/adding_a_page/views/home_page.cljs` and find out.

```clojure
(ns adding-a-page.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   ])
```

Ahh, ok this is a reagent component.

### main application component

So the `#/` route is setting the `:current-page` key to the value of a reagent component called `home-page`.  This is stored in our application's local state (which is held in the `app-state` atom of our sessions namespace).

Wonderful, but how does that help us?  If you look at `src/adding_a_page/core.cljs` this is where everything comes together.  `page-render` will grab the reagent component from `:current-page` and render it.  If we are at the `#/` route, that reagent component will be `home-page`.

```clojure
(ns adding-a-page.core
  (:require [reagent.core :as reagent :refer [atom]]
            [adding-a-page.session :as session :refer [get-state]]
            [adding-a-page.routes :as routes]
            [adding-a-page.views.common :as common]))

(defn page-render []
  [:div
   [common/header]
   [(get-state :current-page)]])  ;; ATTENTION

(defn page-component [] 
  (reagent/create-class {:component-will-mount routes/app-routes
                         :render page-render}))

;; initialize app
(reagent/render-component [page-component]
                          (.getElementById js/document "app"))
```

## Add a new page

Let's create a new file called `src/adding_a_page/views/new_page.cljs` and add the following to it.

```clojure
(ns adding-a-page.views.new-page)

(defn new-page []
  [:div
   [:h2 "New Page"]
   [:div "This is our brand new pagre!"]
   ])
```

So we have created our new page, next we need to add it to the `pages` map in `src/adding_a_page/views/pages.cljs`.

```clojure
(ns adding-a-page.views.pages
  (:require [adding-a-page.views.home-page :refer [home-page]]
            [adding-a-page.views.about-page :refer [about-page]]

;; ATTENTION \/
            [adding-a-page.views.new-page :refer [new-page]]
;; ATTENTION /\
            ))

(def pages {:home-page home-page
            :about-page about-page

;; ATTENTION \/
            :new-page new-page
;; ATTENTION /\
            })
```

After that, it would make sense that if we navigate to `#/new-page` that we see our new page.  Open up `src/adding_a_page/routes.cljs` and add a route for it.

```clojure
(ns adding-a-page.routes
    (:require [secretary.core :as secretary :include-macros true :refer [defroute]]
              [adding-a-page.session :as session]
              [adding-a-page.views.pages :as pages]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; ----------
;; History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
;; need to run this after routes have been defined

;; ----------
;; Routes
(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (session/put! :current-page (pages/pages :home-page))
    (session/put! :nav "home"))

  (defroute "/about" []
    (session/put! :current-page (pages/pages :about-page))
    (session/put! :nav "about"))

;; ATTENTION \/
  (defroute "/new-page" []
    (session/put! :current-page (pages/pages :new-page))
    (session/put! :nav "new"))
;; ATTENTION /\

  (hook-browser-navigation!)
  )
```

Finally, lets go to `src/adding_a_page/views/common.cljs` and add a new button to our nav bar.

```clojure
(ns adding-a-page.views.common
  (:require  [adding-a-page.session :as session :refer [get-state]]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.page-header {:class-name "row"}
   ;; 4 column units
  [:div#title {:class-name "col-md-4"} "adding-a-page"]
   ;; 8 column units
   [:div {:class-name "col-md-8"}
    [:ul.nav.nav-pills 
     [:li {:class (active? (get-state :nav) "home")}  [:a {:href "#/"} [:span {:class-name "fa fa-home"} " Home"]]]
     [:li {:class (active? (get-state :nav) "about")} [:a {:href "#/about"} "About"]]

;; ATTENTION \/
     [:li {:class (active? (get-state :nav) "new")} [:a {:href "#/new-page"} "New Page"]]
;; ATTENTION /\

     ]]])
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
