# Creating a Basic Reagent Component

First things first, what is a component? Let's look at the React.js [documentation](https://facebook.github.io/react/docs/component-specs.html).

```
When creating a component class by invoking `React.createClass()`,
you should provide a specification object that contains a
*render* method and can optionally contain other lifecycle methods
described here.
```

Alright, so a component is created by using the `React.createClass()` method.  At a minimum, it requires a render function.  In [Reagent](https://github.com/reagent-project/reagent/), there is a wrapper for the `React.createClass()` method called `create-class`.  The `create-class` function takes a map of lifecycle methods (`:reagent-render`, `:component-will-mount`, `:component-did-mount`, etc.), see [here](https://github.com/reagent-project/reagent/blob/e53a5c2b1357c0560f0c4c15b28f00d09e27237b/src/reagent/core.cljs#L110).  *Note: (React.js calls it `render`, Reagent calls it `:reagent-render`)*

So if we want to make a bare-bones component, we could do the following.

```clojure
;; Form-3 Component
(defn foo []
  (reagent/create-class {:reagent-render (fn [] [:div "Hello, world!"])}))
```

However, creating a component that only defines the `:reagent-render` lifecycle method is very common. Reagent provides a shorthand for this - a function returning hiccup.

```clojure
;; Form-1 Component
(defn bar []
  [:div "Hello, world!"])
```

Normally, you'd call a function by wrapping it in `( )`. However, reagent components, which produce html, are called by wrapping it in `[ ]` instead. When called, both `foo` and `bar` will invoke `React.createClass()` with the same render method defined. In other words, both `foo` and `bar` are effectively the same reagent component, and, when called, will produce the same html.

But wait, there is yet another way to create a reagent component with just a `render` lifecycle method - a function returning a function that returns hiccup.

```clojure
;; Form-2 Component
(defn baz []
  (fn []
    [:div "Hello, world!"]))
```

In the end, `foo` `bar` and `baz` all produce the same reagent component! For more detail, please read [creating reagent components](https://github.com/Day8/re-frame/wiki/Creating-Reagent-Components).

---

Let's try this all out!  Create a new project.

```
$ lein new rc basic-component
```

Navigate to `src/cljs/basic_component.cljs` and make it look like this.

```clojure
(ns basic-component.core
  (:require [reagent.core :as reagent]))

;; Form-3 Component
(defn foo []
  (reagent/create-class {:reagent-render (fn [] [:div "Hello, world!"])}))

;; Form-1 Component
(defn bar []
  [:div "Hello, world!"])

;; Form-2 Component
(defn baz []
  (fn []
    [:div "Hello, world!"]))

(defn home []
  [:div
   [foo]
   [bar]
   [baz]
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
