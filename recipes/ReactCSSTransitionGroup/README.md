# Problem

You want to use [ReactCSSTransitionGroup](https://facebook.github.io/react/docs/animation.html) in your [reagent](https://github.com/reagent-project/reagent) web application.

(Note: This recipe is based on @jcreedcmu gist in this reagent [issue](https://github.com/reagent-project/reagent/issues/55).)

# Solution

In this example, we will be making a list of items that have an animated transition when they are created and removed.

*Steps*

1. Create a new project
2. Add react-with-addons to dependecies vector in `project.clj`
3. Navigate to `src/cljs/animation/core.cljs` and adapt react's CSSTransitionGroup addon to reagent
4. Create the style for your transition
5. Create the initial app-state
6. Create an `add-item` function
7. Create a `delete-item` function
8. Create a reagent component that can add items, delete items, and displays the items

#### Step 1: Create a new project

```
$ lein new rc animation
```

#### Step 2: Exclude react from reagent and add react-with-addons to dependecies vector in `project.clj`

```clojure
[reagent "0.5.1" :exclusions [cljsjs/react]]
[cljsjs/react-with-addons "0.13.3-0"]
```

#### Step 3: Navigate to `src/cljs/animation/core.cljs` and adapt react's CSSTransitionGroup addon to reagent

```clojure
(def css-transition-group
  (reagent/adapt-react-class js/React.addons.CSSTransitionGroup))
```

#### Step 4: Create the style for your transition

Notice that we are using `foo` as our transition name.

```clojure
(def style
  "li {
  background-color: #44ee22; padding: 10px; margin: 1px; width: 150px;
  border-radius: 5px;
  font-size: 24px;
  text-align: center;
  list-style: none;
  color: #fff;
  height: 2em;
  line-height: 2em;
  padding: 0 0.5em;
  overflow: hidden;
  }

  .foo-enter {
  height: 0;
  transition: height 0.27s ease-out;
  }

  .foo-leave {
  height: 0;
  transition: height 0.27s ease-out;
  }

  .foo-enter-active {
  height: 2em;
  opacity: 1;
  }")
```

#### Step 5: Create the initial app-state

```clojure
(def app-state
  (reagent/atom {:items []
                 :items-counter 0}))
```

#### Step 6: Create an `add-item` function

We are doing two things here: 1) keeping track of the running total of items, and 2) adding the new item to the list of items stored in `app-state`.

```clojure
(defn add-item []
  (let [items (:items @app-state)]
    (swap! app-state update-in [:items-counter] inc)
    (swap! app-state assoc :items (conj items (:items-counter @app-state)))))
```

#### Step 7: Create a `delete-item` function

Here we are are deleting the last item added to the list of items that are stored in `app-state`

```clojure
(defn delete-item []
  (let [items (:items @app-state)]
    (swap! app-state assoc :items (vec (butlast items)))))
```

#### Step 8: Create a reagent component that can add items, delete items, and displays the items

Notice the use of the `css-transition-group` from step 3, and the use of the name `"foo"` from step 4.

```clojure
(defn home []
  [:div
   [:div (str "Total list items to date:  " (:items-counter @app-state))]
   [:button {:on-click #(add-item)} "add"]
   [:button {:on-click #(delete-item)} "delete"]
   [:style style]
   [:ul
   [css-transition-group {:transition-name "foo"}
    (map-indexed (fn [i x]
                   ^{:key i} [:li (str "List Item " x)])
                 (:items @app-state))]]
   ])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
