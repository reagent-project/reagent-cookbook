# Problem

You want to add local storage via [storage-atom](https://github.com/alandipert/storage-atom) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

*Steps*

1. Create a new project
2. Add storage-atom to `project.clj` :dependencies vector
3. Add storage-atom to `core.cljs` namespace
4. Add a reagent atom called `app-state` and wrap it in the `local-storage` function
6. Add an increment button to `home`

#### Step 1: Create a new project

```
$ lein new rc local-storage
```

#### Step 2: Add storage-atom to `project.clj` :dependencies vector

```clojure
[alandipert/storage-atom "1.2.4"]
```

#### Step 3: Add storage-atom to `core.cljs` namespace

Navigate to `src/cljs/local_storage/core.cljs` and update the `ns` to the following.

```clojure
(ns local-storage.core
    (:require [reagent.core :as reagent]
              [alandipert.storage-atom :refer [local-storage]]))
```

#### Step 4: Add a reagent atom called `app-state` and wrap it in the `local-storage` function

```clojure
(def app-state (local-storage 
                (reagent/atom {:counter 0})
                :app-state))
```

#### Step 6: Add an increment button to `home`

```clojure
(defn home []
  [:div
   [:div "Current count: " (@app-state :counter)]
   [:button {:on-click #(swap! app-state update-in [:counter] inc)} 
    "Increment"]])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.

Click on the button, increment the counter, close the page and reopen it ... the current count should persist via local-storate.
