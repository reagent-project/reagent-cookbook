# Problem

You want to add *undo* via [historian](https://github.com/reagent-project/historian) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

*Steps*

1. Create a new project
2. Add Historian to `project.clj` :dependencies vector
3. Add Historian to `core.cljs` namespace
4. Add a reagent atom called `app-state`
5. Have Historian record changes in `app-state`
6. Add an increment and undo button to `home`

#### Step 1: Create a new project

```
$ lein new rc undo
```

#### Step 2: Add Historian to `project.clj` :dependencies vector

```clojure
[historian "1.1.0"]
```

#### Step 3: Add Historian to `core.cljs` namespace

Navigate to `src/cljs/undo/core.cljs` and update the `ns` to the following.

```clojure
(ns undo.core
    (:require [reagent.core :as reagent]
              [historian.core :as hist]))
```

#### Step 4: Add a reagent atom called `app-state`

```clojure
(def app-state (reagent/atom {:counter 0}))
```

#### Step 5: Have Historian record changes in `app-state`

```clojure
(hist/record! app-state :app-state)
```

#### Step 6: Add an increment and undo button to `home`

```clojure
(defn home []
  [:div
   [:div "Current count: " (@app-state :counter)]
   [:button {:on-click #(swap! app-state update-in [:counter] inc)} "Increment"]
   [:button {:on-click #(hist/undo!)} "Undo"] ])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
