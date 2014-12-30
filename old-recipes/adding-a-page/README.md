# Problem

You want to add a new page to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Review reagent-seed template:
    * Review how reagent-seed keeps track of state.
    * Review how reagent-seed does routing.
    * Review main regent component of application.
* Add a new page:
    * Create a new reagent component.
	* Add new component to the pages map.
	* Add route for new component.
	* Add nav button for new component.

Affected files:

* `src/adding_a_page/views/new_page.cljs`
* `src/adding_a_page/views/pages.cljs`
* `src/adding_a_page/routes.cljs`
* `src/adding_a_page/views/common.cljs`

## Create a reagent project

```
$ lein new reagent-seed adding-a-page
```

## Review reagent-seed template

### Keeping track of state

The way that reagent-seed is set up, there is an *atom* called `app-state` (located in `src/adding_a_page/session.cljs`) that contains the global state of our application.

```clojure
(ns adding-a-page.session
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

As you can see, there are few helper functions.  `global-state` helps you get application state from the `app-state` atom.  `global-put!` helps you change the application state of the `app-state` atom.

### Routes

Open `src/adding_a_page/routes.cljs`.  The `app-routes` function says that whenever we navigate to the route `#/`, the `session/global-put!` function will define the `:current-page` in our `session/app-state` atom to be `(pages :home-page)`.

```clojure
(ns adding-a-page.routes
    (:require [secretary.core :as secretary :include-macros true :refer [defroute]]
              [adding-a-page.session :as session :refer [global-put!]]
              [adding-a-page.views.pages :refer [pages]]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

...

;; ----------
;; Routes
(defn app-routes []
  (secretary/set-config! :prefix "#")

;; ATTENTION \/
  (defroute "/" []
    (global-put! :current-page (pages :home-page))
    (global-put! :nav "home"))
;; ATTENTION /\

  (defroute "/about" []
    (global-put! :current-page (pages :about-page))
    (global-put! :nav "about"))

  (hook-browser-navigation!)
  )

```

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

   ])
```

Ahh, ok this is a reagent component!

### Main application component

So the `#/` route is setting the `:current-page` key to the value of a reagent component called `home-page`.  This is stored in global state of our application, which is contained in the `sessions/app-state` atom.

Wonderful, but how does that help us?  If you look at `src/adding_a_page/core.cljs` this is where everything comes together.  `page-render` will grab the reagent component from `:current-page` and render it.  If we are at the `#/` route, that reagent component will be `home-page`.

```clojure
(ns adding-a-page.core
  (:require [reagent.core :as reagent]
            [adding-a-page.session :as session :refer [global-state]]
            [adding-a-page.routes :as routes]
            [adding-a-page.views.common :as common]))

(defn page-render []
  [:div.container
   [common/header]
   [(global-state :current-page)]])

(defn page-component [] 
  (reagent/create-class {:component-will-mount routes/app-routes
                         :render page-render}))

;; initialize app
(reagent/render-component [page-component]
                          (.getElementById js/document "app"))
```

## Adding a new page

### Create a new reagent component

Let's create a new file called `src/adding_a_page/views/new_page.cljs` and add the following reagent component (`new-page`) to it.

```clojure
(ns adding-a-page.views.new-page)

(defn new-page []
  [:div
   [:h2 "New Page"]
   [:div "This is our brand new page!"]
   ])
```

### Add new component to the pages map

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

### Add route for new component

After that, it would make sense that if we navigate to `#/new-page` that we see our new page.  Open up `src/adding_a_page/routes.cljs` and add a route for it.

```clojure
(ns adding-a-page.routes
    (:require [secretary.core :as secretary :include-macros true :refer [defroute]]
              [adding-a-page.session :as session :refer [global-put!]]
              [adding-a-page.views.pages :refer [pages]]
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
    (global-put! :current-page (pages :home-page))
    (global-put! :nav "home"))

  (defroute "/about" []
    (global-put! :current-page (pages :about-page))
    (global-put! :nav "about"))

;; ATTENTION \/
  (defroute "/new" []
    (global-put! :current-page (pages :new-page))
    (global-put! :nav "new"))
;; ATTENTION /\

  (hook-browser-navigation!)
  )
```

### Add nav button for new component

Finally, lets go to `src/adding_a_page/views/common.cljs` and add a new button to our nav bar.

```clojure
(ns adding-a-page.views.common
  (:require  [adding-a-page.session :as session :refer [global-state]]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.page-header.row

   [:div#title.col-md-6 
    "adding-a-page"]

   [:div.col-md-6
    [:ul.nav.nav-pills 
     [:li {:class (active? (global-state :nav) "home")}  [:a {:href "#/"} [:span.fa.fa-home " Home"]]]
     [:li {:class (active? (global-state :nav) "about")} [:a {:href "#/about"} "About"]]
;; ATTENTION \/
     [:li {:class (active? (global-state :nav) "new")} [:a {:href "#/new"} "New Page"]]]]
;; ATTENTION /\
   ])
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
