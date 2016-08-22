# Problem

You want to add routing using [Secretary](https://github.com/gf3/secretary) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

*Steps*

1. Create a new project
2. Add Secretary to `project.clj` :dependencies vector
3. Add Secretary to `core.cljs` namespace
4. Add browser history
5. Create routes
6. Create `home` and `about` pages that link to each other
7. Create a `current-page` multimethod that will return a reagent component based on a keyword
8. Update `page` to rely on current-page
9.  Update `main` to run `app-routes`

#### Step 1: Create a new project

```
$ lein new rc add-routing
```

#### Step 2: Add Secretary to `project.clj` :dependencies vector

```clojure
[secretary "1.2.3"]
```

#### Step 3: Add Secretary to `core.cljs` namespace

Navigate to `src/cljs/add_routing/core.cljs` and update the `ns` to the following.

```clojure
(ns add-routing.core
    (:require-macros [secretary.core :refer [defroute]])
    (:import goog.History)
    (:require [secretary.core :as secretary]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [reagent.core :as reagent]))
```

#### Step 4: Add browser history

Add browser history.

```clojure
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
```

#### Step 5: Create routes

Secretary is a client-side router for clojurscript. We are going to create two routes and prefix them with `#`.  Instead of putting a reagent component in app-state, we are instead going to put a keyword (such as `:home` or `:about`) that represents a reagent component in app-state. Later, we will create a dispatch function (i.e., a `defmulti`) that will grab the appropriate reagent component by keyword.

```clojure
(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (swap! app-state assoc :page :home))

  (defroute "/about" []
    (swap! app-state assoc :page :about))

  (hook-browser-navigation!))
```

#### Step 6: Create `home` and `about` pages that link to each other

```clojure
(defn home [ratom]
  [:div [:h1 "Home Page"]
   [:a {:href "#/about"} "about page"]])

(defn about [ratom]
  [:div [:h1 "About Page"]
   [:a {:href "#/"} "home page"]])
```

#### Step 7: Create a `current-page` multimethod that will return a reagent component based on a keyword

```clojure
(defmulti page identity)
(defmethod page :home [] home)
(defmethod page :about [] about)
(defmethod page :default [] (fn [_] [:div]))
```

#### Step 8: Update `page` to rely on current-page

```clojure
(defn page [ratom]
  (let [page-key (:page @ratom)]
    [(current-page page-key) ratom]))
```

#### Step 9: Update `main` to run `app-routes`

```clojure
(defn ^:export main []
  (app-routes)
  (reload))
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
