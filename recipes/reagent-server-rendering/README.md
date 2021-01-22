# reagent-server-rendering

## Problem

You want to Reagent to generate HTML on the server and send a rendered page to the browser.

## Solution

We're going to follow [this example](https://carouselapps.com/2015/09/11/isomorphic-clojurescriptjavascript-for-pre-rendering-single-page-applications-part-1/) using Nashorn to render Reagent on the server.

*Steps*

1. Create a new project
2. Add the necessary dependencies and plugins to `project.clj`
3. Add a Clojure namespace for the server functions
4. Add the functions to create the Nashorn JavaScript engine and render Reagent components
5. Create a function to render the HTML page using Hiccup
6. Define server routes for the application
7. Modify the ClojureScript namespace to provide functions to render the pages on the server and the client

### Step 1: Create a new project

    lein new rc reagent-server-rendering

### Step 2: Add the necessary dependencies and plugins to `project.clj`

```clojure
:dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [reagent "0.5.1"]
                 ;;Ring is needed to start an HTTP server
                 [ring "1.4.0"]
                 ;;provides default middleware for Ring
                 [ring/ring-defaults "0.1.5"]
                 ;;server-side HTML templating
                 [hiccup "1.0.5"]
                 ;;thread pool management
                 [aleph "0.4.0"]]

  :plugins [[lein-cljsbuild "1.0.6"]
            ;;plugin for starting the HTTP server
            [lein-ring "0.9.6"]]

  ;;specifies the HTTP handler
  :ring {:handler reagent-server-rendering.handler/app}
```

### Step 3: Add a Clojure namespace for the server functions

We need to create a Clojure namespace that will house the functions needed to start the JavaScript engine and render the HTML.
We'll create a new file `src/clj/reagent_server_rendering/handler.clj`. The namespace will have the following references in its declaration.

```clojure
(ns reagent-server-rendering.handler
  (:require [aleph.flow :as flow]
            [clojure.java.io :as io]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]])
  (:import [io.aleph.dirigiste Pools]
           [javax.script ScriptEngineManager Invocable]))
```

### Step 4: Add the functions to create the Nashorn JavaScript engine and render Reagent components

```clojure
(defn- create-js-engine []
  (doto (.getEngineByName (ScriptEngineManager.) "nashorn")
    ; React requires either "window" or "global" to be defined.
    (.eval "var global = this")
    (.eval (-> "public/js/compiled/app.js"
               io/resource
               io/reader))))

; We have one and only one key in the pool, it's a constant.
(def ^:private js-engine-key "js-engine")
(def ^:private js-engine-pool
  (flow/instrumented-pool
    {:generate   (fn [_] (create-js-engine))
     :controller (Pools/utilizationController 0.9 10000 10000)}))

(defn- render-page [page-id]
  (let [js-engine @(flow/acquire js-engine-pool js-engine-key)]
    (try (.invokeMethod
           ^Invocable js-engine
           (.eval js-engine "reagent_server_rendering.core")
           "render_page" (object-array [page-id]))
         (finally (flow/release js-engine-pool js-engine-key js-engine)))))
```

The `create-js-engine` function will setup a new instance of the engine that will run the compiled JavaScript produced by compiling ClojureScript.

The `js-engine-pool` keeps a pool of running engines.

The `render-page` function will acquire an engine from the pool and call the `render-page` function from the `reagent-server-rendering.core` ClojureScript namespace. It will pass this function the page id as the argument. Once the page is rendered the JavaScript engine is released back into the pool.

### Step 5: Create a function to render the HTML page using Hiccup

```clojure
(defn page [page-id]
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css "css/site.css")]
    [:body
     [:div#app
      (render-page page-id)]
     (include-js "js/compiled/app.js")
     [:script {:type "text/javascript"}
      (str "reagent_server_rendering.core.main('" page-id "');")]]]))
```

The `page` function uses Hiccup to generate a static page and inject the generated HTML produced by the `render-page` function into it.

### Step 6: Define server routes for the application

We'll now define routes for `/` and `/about` pages. Each route will pass a unique page id to the `page` function:

```clojure
(defroutes app-routes
  (GET "/" [] (page "home"))
  (GET "/about" [] (page "about"))
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-defaults app-routes site-defaults))
```

### Step 7: Modify the ClojureScript namespace to provide functions to render the pages on the server and the client

```clojure
(ns reagent-server-rendering.core
    (:require [reagent.core :as reagent]
              [reagent.dom :as rdom]
              [reagent.dom.server :as reagent-server]))

(defn home-page []
  [:div [:h2 "Welcome to reagent-server-rendering"]
   [:div [:a {:href "/about"} "go to about page"]]])

(defn about-page []
  [:div [:h2 "About reagent-server-rendering"]
   [:div [:a {:href "/"} "go to the home page"]]])

(def pages
  {"home"  home-page
   "about" about-page})

(defn ^:export render-page [page-id]
  (reagent-server/render-to-string [(get pages page-id)]))

(defn ^:export main [page-id]
  (rdom/render [(get pages page-id)] (.getElementById js/document "app")))
```

The `render-page` function will render the HTML string given the page id that will be served from the server. The `main` function will render Reagent components at runtime when ClojureScript is loaded in the browser.

# Usage

```bash
# watch files for changes and rebuild...
lein cljsbuild auto

# ...or only build once
lein cljsbuild once

# start the server (in another terminal if you watch)
lein ring server-headless
```

Open [localhost:3000](http://localhost:3000) to see the page rendered.

If you're seeing an error like this: `java.lang.IllegalArgumentException: No implementation of method: :make-reader of protocol: #'clojure.java.io/IOFactory found for class: nil`, you probably forgot to build cljs, or there was an error building it.
