# Problem

You want to filter a table based on user input.

# Solution

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Include `clojure.string`
4. Create a reagent atom called `app-state` holds the table data
5. Setup filter-content
6. Create the `table` component
7. Create the `search-table` component
8. Add `search-table` to home

#### Step 1: Create a new project

```
$ lein new rc filter-table
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <title>Filter Table</title>
  <body>
    <div class"container">
      <br>
      <div id="app">
    </div>

    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  </div>
   <script src="js/compiled/app.js" type="text/javascript"></script>
   <script>filter_table.core.main();</script>
  </body>
</html>
```

From here on out we will be working in `src/cljs/filter_table/core.cljs`

#### Step 3: Include `clojure.string`

We will use [clojure.string] to convert user input and the first-name to upper-case
this will ensure we can search the entire field.

```clojure
(ns filter-table.core
  (:require [reagent.core :as reagent]
            [clojure.string :as string]))
```

#### Step 4: Create a reagent atom called `app-state`


We store our table content in a reagent atom.

```clojure
(def app-state
     (reagent/atom [{:id 1 :first-name "Jason" :last-name "Yates" :age "34"}
                    {:id 2 :first-name "Chris" :last-name "Wilson" :age "33"}
                    {:id 3 :first-name "John" :last-name "Lawrence" :age"32"}
                    {:id 4 :first-name "Albert" :last-name "Voxel" :age "67"}
                    {:id 5 :first-name "Zemby" :last-name "Alcoe" :age "495"}]))
```

#### Step 5:  Setup the filter-content

The filter-content function will take a filterstring and return the filtered app-state

```clojure
(defn filter-content
  [filterstring]
  (filter #(re-find (->> (str filterstring)
                         (string/upper-case)
                         (re-pattern))
                    (string/upper-case (:first-name %)))
          @app-state))
```

#### Step 6: Create the `table` component

The table component takes the filter provided by the search-table component
and returns the filtered app-state in a table.

```clojure
(defn table
  [myfilter]
  [:table {:class "table table-condensed"}
   [:thead
    [:tr
     [:th "First Name"]
     [:th "Last Name"]
     [:th "Age"]]]
    [:tbody
     (for [{:keys [id
                   first-name
                   last-name
                   age]} (filter-content myfilter)]
       ^{:key id}
       [:tr
         [:td first-name]
         [:td last-name]
         [:td age]])]])
```

#### Step 7: Create the `search-table` component

The search-table component gives us a text input which get's sent to the
table component to filter the table.

```clojure
(defn search-table
  []
  (let [filter-value (reagent/atom nil)]
    (fn []
      [:div
       [:input {:type      "text" :value @filter-value
                :on-change #(reset! filter-value (-> % .-target .-value))}]
       [table @filter-value]])))
```

#### Step 8: Add `search-table` to home

```clojure
(defn home []
  [:div.container
   [search-table]])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
