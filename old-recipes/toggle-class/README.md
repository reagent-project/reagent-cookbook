# Problem

You want to toggle the class of an element (without using jQuery) in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to store the class of an element in a reagent atom, and then toggle the class when a button is clicked.

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Toggle class of a button in `home-page` component
    * Add an atom to track local state of home-page component
	* Create a button
	* Create function to toggle class of button
	* Make atom a reagent atom

Affected files:

* `src/toggle_class/views/home_page.cljs`

## Create a reagent project

```
$ lein new reagent-seed adding-a-page
```

## Toggle class of a button in the home-page component

Toggling the class of a button doesn't seem like it needs to be stored in the global state of our application (which is stored in the `app-state` atom of the `toggle-class.session` namespace).  Instead, I think we should create an atom local to the `home-page` component.  Navigate to `src/toggle_class/views/home_page.cljs

### Add an atom to track local state of home-page component

Ok, let's add that atom.  Inside the atom we can create a key (I'm going to call it `:btn-class`) with a value set to the initial class of our button (`btn btn-default`).

```clojure
(ns toggle-class.views.home-page)

;; ATTENTION \/
(defn home-page []
  (let [state (atom {:btn-class "btn btn-default"})]
    (fn []
      [:div
       [:h2 "Home Page"]
       [:div "Woot! You are starting a reagent application."]
       ])))
;; ATTENTION /\
```

### Create a button

Next, we want to create a button.

```clojure
(ns toggle-class.views.home-page)

(defn home-page []
  (let [state (atom {:btn-class "btn btn-default"})]
    (fn []
      [:div
       [:h2 "Home Page"]
       [:div "Woot! You are starting a reagent application."]

;; ATTENTION \/
       [:div {:class (@state :btn-class)} "Click me"]
;; ATTENTION /\
       ])))
```

### Create function to toggle class of button

The above code will initialize the class of the button to be `btn btn-default`. However, we want to toggle the class of the button when the button is clicked. We can accomplish that as follows.

```clojure
(ns toggle-class.views.home-page)

;; ATTENTION 1 of 2 \/
(defn toggle-class [a k class1 class2]
  (if (= (@a k) class1)
    (swap! a assoc k class2)
    (swap! a assoc k class1)))
;; ATTENTION 1 of 2 /\

(defn home-page []
  (let [state (atom {:btn-class "btn btn-default"})]
    (fn []
      [:div
       [:h2 "Home Page"]
       [:div "Woot! You are starting a reagent application."]
       [:div {:class (@state :btn-class)

;; ATTENTION 2 of 2 \/
              :on-click (fn [] (toggle-class state :btn-class "btn btn-default" "btn btn-danger"))
;; ATTENTION 2 of 2 /\

              } "Click me"]])))
```

### Make atom a reagent atom

Doh! This actually doesn't work because the page won't be re-rendered when the atom changes.  To re-render the page, we need to make the atom a reagent atom!  Let's include reagent and refer the reagent atom.

```clojure
(ns toggle-class.views.home-page
;; ATTENTION \/
  (:require [reagent.core :as reagent :refer [atom]]))
;; ATTENTION /\

(defn toggle-class [a k class1 class2]
  (if (= (get @a k) class1)
    (swap! a assoc k class2)
    (swap! a assoc k class1)))

(defn home-page []
  (let [state (atom {:btn-class "btn btn-default"})]  ; now this atom is a reagent atom!
    (fn []
      [:div
       [:h2 "Home Page"]
       [:div "Woot! You are starting a reagent application."]

       [:div {:class (@state :btn-class)
              :on-click (fn [] (toggle-class state :btn-class "btn btn-default" "btn btn-danger"))
              } "Click me"]])))
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

