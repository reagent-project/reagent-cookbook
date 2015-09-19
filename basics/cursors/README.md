# Reagent Cursors

In Reagent, it is a common pattern to create a single reagent atom (often called `app-state` or `app-db`) that contains all of your application's state.  [Cursors](https://github.com/reagent-project/reagent/blob/e53a5c2b1357c0560f0c4c15b28f00d09e27237b/src/reagent/core.cljs#L248) are an effective way to get a pointer inside a larger reagent atom, such as `app-state`.

Let's start by making a reagent atom.

```clojure
(def app-state (reagent/atom {:foo {:bar "Hello, world!"
                                    :baz {:quux "Woot"}}}))
```

To see what is inside `app-state` we could create a reagent component that dereferences the atom and converts it to a string.

```clojure
(defn inside-app-state []
  [:div (str "Inside app-state: " @app-state)])

;; Inside app-state: {:foo {:bar "Hello, world!", :baz {:quux "Woot"}}}
```

Next, let's create a cursor and a reagent component to look inside.

```clojure
(def foo-cursor (reagent/cursor app-state [:foo]))

(defn inside-foo-cursor []
  [:div (str "Inside foo-cursor: " @foo-cursor)])
```

The `reagent/cursor` function takes a reagent atom and a path inside that reagent atom.  The path we provided was `[:foo]`. Notice how it is wrapped in `[ ]`, this is just a convenient way to traverse a nested map structure.  Let's make a few more cursors to understand this a little better.

```clojure
(def foo-cursor (reagent/cursor app-state [:foo]))

(defn inside-foo-cursor []
  [:div (str "Inside foo-cursor: " @foo-cursor)])

;; Inside foo-cursor: {:bar "Hello, world!", :baz {:quux "Woot"}}


(def foobar-cursor (reagent/cursor app-state [:foo :bar]))

(defn inside-foobar-cursor []
  [:div (str "Inside foobar-cursor: " @foobar-cursor)])

;; Inside foobar-cursor: Hello, world!


(def foobaz-cursor (reagent/cursor app-state [:foo :baz]))

(defn inside-foobaz-cursor []
  [:div (str "Inside foobaz-cursor: " @foobaz-cursor)])

;; Inside foobaz-cursor: {:quux "Woot"}


(def foobazquux-cursor (reagent/cursor app-state [:foo :baz :quux]))

(defn inside-foobazquux-cursor []
  [:div (str "Inside foobazquux-cursor: " @foobazquux-cursor)])

;; Inside foobazquux-cursor: Woot
```

Take a look at the foobazquux-cursor, the path is `[:foo :baz :quux]` and we can see it will return "Woot".  If you look back at `app-state` you should have a sense of how this path thing works now.

---

Let's create a project and try this out.

```
$ lein new rc basic-component
```

Navigate to `src/cljs/cursors.cljs` and make it look like this.

```clojure
(ns cursors.core
    (:require [reagent.core :as reagent]))

(def app-state (reagent/atom {:foo {:bar "Hello, world!"
                                    :baz {:quux "Woot"}}}))

(defn inside-app-state []
  [:div (str "Inside app-state: " @app-state)])

(def foo-cursor (reagent/cursor app-state [:foo]))

(defn inside-foo-cursor []
  [:div (str "Inside foo-cursor: " @foo-cursor)])

(def foobar-cursor (reagent/cursor app-state [:foo :bar]))

(defn inside-foobar-cursor []
  [:div (str "Inside foobar-cursor: " @foobar-cursor)])

(def foobaz-cursor (reagent/cursor app-state [:foo :baz]))

(defn inside-foobaz-cursor []
  [:div (str "Inside foobaz-cursor: " @foobaz-cursor)])

(def foobazquux-cursor (reagent/cursor app-state [:foo :baz :quux]))

(defn inside-foobazquux-cursor []
  [:div (str "Inside foobazquux-cursor: " @foobazquux-cursor)])

(defn home []
  [:div
   [inside-app-state]
   [inside-foo-cursor]
   [inside-foobar-cursor]
   [inside-foobaz-cursor]
   [inside-foobazquux-cursor]
   ])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))
```

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
