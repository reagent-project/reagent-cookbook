# Problem

You want to connect to [firebase](https://www.firebase.com/) in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to connect firebase to a text input field.

## Create a reagent project

Let's start off with the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed firebase
```

## Add firebase to index.html

Add firebase to your `resources/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>firebase</title>
  </head>
  <body class="container">

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>

<!-- Attention \/ -->
    <!-- firebase -->
    <script src="https://cdn.firebase.com/js/client/2.0.2/firebase.js"></script>
<!-- ATTENTION /\ -->

    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
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

## Adding firebase to home-page component

I think we should add firebase to the home page, but first, let's take a look at what is already there.

```clojure
(ns firebase.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]
   [:div "Woot! You are starting a reagent application."]
   ])
```

To add firebase, we need to add a [reference](https://www.firebase.com/docs/web/guide/understanding-data.html) to our firebase location of interest.

```clojure
(ns firebase.views.home-page)

(defn home-page []
  (let [fb (js/Firebase. "FIXME")]  ;; REPLACE FIXME WITH YOUR FIREBASE REFERENCE
    [:div
     [:h2 "Home Page"]
     [:div "Woot! You are starting a reagent application."]
     ]))
```

## Create an input-field function

Create a function `input-field` that takes two arguments: a value and a firebase reference. The goal here is to create an input field and to update firebase whenever the input is changed.

```clojure
(ns firebase.views.home-page)

;; ATTENTION \/
(defn input-field [value fb]
  [:input {:type "text"
           :value value
           :on-change #(on-change % fb)}]) ;; note: on-change has not been defined yet
;; ATTENTION /\

(defn home-page []
  (let [fb (js/Firebase. "FIXME")]  ;; REPLACE FIXME WITH YOUR FIREBASE REFERENCE
    [:div
     [:h2 "Home Page"]
     [:div "Woot! You are starting a reagent application."]
     ]))
```

## Send value to Firebase on event change

Let's start building our `on-change` function. The first thing we want to do is send the input value to firebase.  We can do this with `.set` from the [firebase api](https://www.firebase.com/docs/web/api/firebase/set.html).  As you can see below, we are setting the key `"text-from-app"` to the value of our input field.

```clojure
(ns firebase.views.home-page)

;; ATTENTION \/
(defn on-change [event fb] 
  (.set fb (clj->js {:text-from-app (-> event .-target .-value)}))
  )
;; ATTENTION /\

(defn input-field [value fb]
  [:input {:type "text"
           :value value
           :on-change #(on-change % fb)}])

(defn home-page []
  (let [fb (js/Firebase. "FIXME")]  ;; REPLACE FIXME WITH YOUR FIREBASE REFERENCE
    [:div
     [:h2 "Home Page"]
     [:div "Woot! You are starting a reagent application."]
     ]))
```

## Listen to Firebase and place changes in local sate

Next, we want to complete the circle. If a value is sent to firebase (or if a value is changed in firerbase), we want to hear about it back in our webapp.  We can do this by asking firebase to send us a *snapshot* of the *value* *on* a change via the `.on` function.  With that *snapshot* we can find the value we want and place it in our local state.  This is done using the `session/put!` function which associates a key-value pair in the `session/app-state` atom (which is where our local state is stored).

```clojure
(ns firebase.views.home-page
;; ATTENTION \/
  (:require [firebase.session :as session :refer [get-state]])
;; ATTENTION /\
  )

(defn on-change [event fb] 
  (.set fb (clj->js {:text-from-app (-> event .-target .-value)}))
;; ATTENTION \/
  (.on fb "value" (fn [snapshot] 
                    (session/put! :my-text ((js->clj (.val snapshot)) "text-from-app"))))
;; ATTENTION /\
  )

...
```

## Use the input-field function

Finally, we want to use our `input-field` function, which will call the `on-change` function whenever there is a change in the text input field.

```clojure
(ns firebase.views.home-page
  (:require [firebase.session :as session :refer [get-state]]))

(defn on-change [event fb] 
  (.set fb (clj->js {:text-from-app (-> event .-target .-value)}))
  (.on fb "value" (fn [snapshot] 
                    (session/put! :my-text ((js->clj (.val snapshot)) "text-from-app")))))

(defn input-field [value fb]
  [:input {:type "text"
           :value value
           :on-change #(on-change % fb)}])

(defn home-page []
  (let [fb (js/Firebase. "FIXME")]  ;; REPLACE FIXME WITH YOUR FIREBASE REFERENCE
    [:div
     [:h2 "Home Page"]
     [:div "Woot! You are starting a reagent application."]

;; ATTENTION \/
     [:div
      [:p "The value is now: " (get-state :my-text)]
      [:p "Change it here: " [input-field (get-state :my-text) fb]]]
;; ATTENTION /\
```

# Usage

To view our app, we need to perform the following steps:

Navigate to `src/firebase/views/home_page.cljs` and change the *FIXME* to your firebase url.  (Also make sure your firebase security rules allow reading and writing).

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
