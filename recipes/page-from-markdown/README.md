# Problem

You want to add a new page from *markdown* to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to use macros in clojure and the [markdown-clj](https://github.com/yogthos/markdown-clj) library to add a page from markdown to our reagent webapp.

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add markdown-clj library to `project.clj`
* Add markdown file
* Create macro to parse markdown file and turn it into a string
* Create function to render html from a string
* Add markdown page to our map of pages
* Add route for our new markdown page
* Add button to navigate to markdown page

Affected files:

* `project.clj`
* `resources/pulic/page-from-markdown.md`
* `src/page_from_markdown/util/macros.clj`
* `src/page_from_markdown/util/elem.cljs`
* `src/page_from_markdown/views/pages.cljs`
* `src/page_from_markdown/routes.cljs`
* `src/page_from_markdown/views/common.cljs`

Prerequisite Recipes:

* [adding a page](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/adding-a-page) recipe.

## Create a reagent project

```
$ lein new reagent-seed page-from-markdown
```

## Add markdown-clj library to project.clj

First, let's add the [markdown-clj](https://github.com/yogthos/markdown-clj) library to our `project.clj` file.

```clojure
(defproject page-from-markdown "0.1.0-SNAPSHOT"
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
                 ;; Markdown parser
                 [markdown-clj "0.9.54"] ]
;; ATTENTION /\

...
```

## Add markdown file

Next, let's add a markdown file that we want to be rendered as a page.  Create a new file here `resources/public/page-from-markdown.md`, and add the following markdown to it.

```markdown
## Page from Markdown

Hello world! I am a page from markdown.

### Sub-heading

This is some more text. Also, here are some bullets:

* bullet 1
* bullet 2
* bullet 3
```

## Create macro to parse markdown file and turn it into a string

I may not be in the majority, but I don't mind mixing `.clj` files with `.cljs` files in the same directory if they are all working toward the same thing. For example, we want to create a macro in a `.clj` file to be used by our `.cljs` files, so to me, it makes sense to keep all of these files in the same parent directory.  Let's create the following folder and file: `src/page_from_markdown/util/macros.clj`.

```clojure
(ns page-from-markdown.util.macros
  (:use [markdown.core :only [md-to-html-string]]))

(defmacro defmd
  [symbol-name html-name]
  (let [base "resources/public/"
        content  (md-to-html-string (slurp (str base html-name)))]
    `(defn ~symbol-name []
       [:div (elem/dangerous :div '~content)])))

;; Note: elem/dangerous is a clojurescript function that we need to include whenever we use this macro!
```

## Create function to render html from a string

Next, we need to make the `elem/dangerous` clojurescript funtion.  Let's create a new file `src/page_from_markdown/util/elem.cljs`.

```clojure
(ns page-from-markdown.util.elem)

(defn dangerous
  ([comp content]
     (dangerous comp nil content))
  ([comp props content]
     [comp (assoc props :dangerouslySetInnerHTML {:__html content})]))
```

This is a neat function that allows you to render html text as opposed to displaying it. I found it from this [reagent issue](https://github.com/reagent-project/reagent/issues/14).

## Add markdown page to our map of pages

Navigate to `src/page_from_markdown/views/pages`.  This is where we define which page is being displayed. If you are unfamiliar with the pages map, I would recommend reading the [adding a page](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/adding-a-page) recipe.  Let's add our markdown page.

```clojure
(ns page-from-markdown.views.pages
  (:require [page-from-markdown.views.home-page :refer [home-page]]
            [page-from-markdown.views.about-page :refer [about-page]]

;; ATTENTION \/
            [page-from-markdown.util.elem :as elem]) ;this is used in defmd macro
            (:require-macros [page-from-markdown.util.macros :refer [defmd]]))

(defmd page-from-markdown "page-from-markdown.md") ; this will return a reagent component

(def pages {:home-page home-page
            :about-page about-page
            :page-from-markdown page-from-markdown
            })
;; ATTENTION /\
```

## Add route for our new markdown page

Now that we have added our markdown page to the pages map, we need to add a route to display that page.  Go to `src/page_from_markdown/routes.cljs`.

```clojure
(ns page-from-markdown.routes
    (:require [secretary.core :as secretary :include-macros true :refer [defroute]]
              [page-from-markdown.session :as session :refer [global-put!]]
              [page-from-markdown.views.pages :refer [pages]]
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
  (defroute "/page-from-markdown" []
    (global-put! :current-page (pages :page-from-markdown))
    (global-put! :nav "page-from-markdown"))
;; ATTENTION /\

  (hook-browser-navigation!)
  )
```

## Add button to navigate to markdown page

Finally, let's add a button to allow us to navigate to our markdown page.

```clojure
(ns page-from-markdown.views.common
  (:require  [page-from-markdown.session :as session :refer [global-state]]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.page-header.row

  [:div#title.col-md-6 
   "page-from-markdown"]

   [:div.col-md-6
    [:ul.nav.nav-pills 
     [:li {:class (active? (global-state :nav) "home")}  [:a {:href "#/"} [:span.fa.fa-home " Home"]]]
     [:li {:class (active? (global-state :nav) "about")} [:a {:href "#/about"} "About"]]
;; ATTENTION \/
     [:li {:class (active? (global-state :nav) "page-from-markdown")} [:a {:href "#/page-from-markdown"} "Markdown"]]]]
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
