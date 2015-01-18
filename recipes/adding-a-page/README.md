# Problem

You want to add a page using [Secretary](https://github.com/gf3/secretary) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

[Demo](http://rc-adding-a-page2.s3-website-us-west-1.amazonaws.com/) [Video](https://www.youtube.com/watch?v=D7uwDUUngy0)

*Steps*

1. Create a new project
2. Add Secretary to `project.clj` :dependencies vector
3. Create a reagent atom to keep track of application state
4. Add helper function to `put!` state into our reagent atom
5. Add a new page called `about`
6. Add link on `home` to `about`
7. Add a reagent component that will display the `:current-page`
8. Add Secretary to namespace of `src/cljs/adding_a_page/core.cljs`
9. Define `#` prefix for routes
10. Create routes for `home` and `about`
11. Add browser history
12. Change the initially rendered component to `current-page`

#### Step 1: Create a new project

```
$ lein new rc adding-a-page
```

#### Step 2: Add Secretary to `project.clj` :dependencies vector

```clojure
[secretary "1.2.1"]
```

#### Step 3: Create a reagent atom to keep track of application state

Navigate to `src/cljs/adding_a_page/core.cljs`.

```clojure
(def app-state (reagent/atom {}))
```

#### Step 4: Add helper function to `put!` state into our reagent atom

```clojure
(defn put! [k v]
  (swap! app-state assoc k v))
```

#### Step 5: Add a new page called `about`

```clojure
(defn about []
  [:div "This is the about page."
   [:div [:a {:href "#/"} "go to the home page"]]])
```

#### Step 6: Add link on `home` to `about`

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div [:a {:href "#/about"} "go to the about page"]]])
```

#### Step 7: Add a reagent component that will display the `:current-page`

```clojure
(defn current-page-will-mount []
  (put! :current-page home))

(defn current-page-render []
  [(@app-state :current-page)])

(defn current-page []
  (reagent/create-class {:component-will-mount current-page-will-mount
                         :render current-page-render}))
```

#### Step 8: Add Secretary to namespace of `src/cljs/adding_a_page/core.cljs`

```clojure
(ns adding-a-page.core
  (:require [reagent.core :as reagent]
            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))
```

#### Step 9: Define `#` prefix for routes

```clojure
(secretary/set-config! :prefix "#")
```

#### Step 10: Create routes for `home` and `about`

```clojure
(defroute "/" []
  (put! :current-page home))

(defroute "/about" []
  (put! :current-page about))
```

#### Step 11: Add browser history

```clojure
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(hook-browser-navigation!)
```

#### Step 12: Change the initially rendered component to `current-page`

```clojure
(reagent/render-component [current-page]
                          (.getElementById js/document "app"))
```

# Usage

Compile cljs files.

```
$ lein cljsbuild once
```

Start a server.

```
$ lein ring server
```
