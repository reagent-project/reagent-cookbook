# Component-level State

It is encouraged that most/all of your application's state is held in a top-level reagent atom (often called `app-state` or `app-db`). However, sometimes it just makes more sense to have component-level state (e.g., whether or not a button is toggled).

To create component-level state, we are going to create a [Form-2](https://github.com/Day8/re-frame/wiki/Creating-Reagent-Components#form-2--a-function-returning-a-function) reagent component.  In other words, we are going to create a function, that returns a function, that returns hiccup.


```clojure
;; Do this
(defn foo []  ;; A function
  (let [component-state (reagent/atom {:count 0})] ;; <-- not included in render function
    (fn []  ;; That returns a function  <-- render function is from here down
      [:div ;; That returns hiccup
       [:p "Current count is: " (get @component-state :count)]
       [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]])))
```

The declaration of `component-state` in the outer `let` block is **not** included in the *render* function.  This is important! This means that the `component-state` is only created once, and it is not recreated with every render phase.

Not including the inner function is a common mistake.

```clojure
;; Don't do this
(defn foo-mistake []
  (let [component-state (reagent/atom {:count 0})]
    [:div
     [:p "Current count is: " (get @component-state :count)]
     [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))
```

If you try to click the increment button in `foo-mistake` you will never see the counter increment. This is because the reagent atom, `component-state` is getting recreated with every re-render (and `:count` is therefore never successfully incremented).  Just to confirm that the component is actually re-rendering on-click, let's add a `console.log`.

```clojure
;; Don't do this
(defn foo-mistake2 []
  (let [component-state (reagent/atom {:count 0})]
    [:div
     [:p "Current count is: " (get @component-state :count)] ;; <-- This deref is causing the re-render
     (.log js/console (str "Foo Mistake 2 is being rendered"))
     [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))
```

Confirmed.  When we click the button, we see the message printed to the console.

You may have noticed that in `foo` we were doing `(get @component-state :count)` inline.  There is nothing wrong with pulling this out into a `let` block ... just make sure it is inside the inner function.

```clojure
;; Do this
(defn foo-inner-let []
  (let [component-state (reagent/atom {:count 0})]
    (fn [] ;; <-- `render` is from here down
      (let [count (get @component-state :count)] ;; let block is inside `render`
        [:div
         [:p "Current count is: " count]
         [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))))
```

---

Let's create a project and try this out.

```
$ lein new rc component-level-state
```

Navigate to `src/cljs/component_level_state/core.cljs` and make it look like this.

```clojure
(ns component-level-state.core
    (:require [reagent.core :as reagent]))

;; Do this
(defn foo []  ;; A function
  (let [component-state (reagent/atom {:count 0})] ;; <-- not included in `render`
    (fn []  ;; That returns a function  <-- `render` is from here down
      [:div ;; That returns hiccup
       [:p "Current count is: " (get @component-state :count)]
       [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]])))

;; Don't do this
(defn foo-mistake []
  (let [component-state (reagent/atom {:count 0})]
    [:div
     [:p "Current count is: " (get @component-state :count)]
     [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))

;; Don't do this
(defn foo-mistake2 []
  (let [component-state (reagent/atom {:count 0})]
    [:div
     [:p "Current count is: " (get @component-state :count)] ;; <-- This deref is causing the re-render
     (.log js/console (str "Foo Mistake 2 is being rendered")) ;; <- will print this on-click
     [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))

;; Do this
(defn foo-inner-let []
  (let [component-state (reagent/atom {:count 0})]
    (fn [] ;; <-- `render` is from here down
      (let [count (get @component-state :count)] ;; let block is inside `render`
        [:div
         [:p "Current count is: " count]
         [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]]))))

(defn home []
  [:div
   [:h1 "Foo"]
   [foo]
   [:h1 "Foo Mistake"]
   [foo-mistake]
   [:h1 "Foo Mistake 2"]
   [foo-mistake2]
   [:h1 "Foo Inner Let"]
   [foo-inner-let]
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
