# Goal

Create a table that allows sorting on columns and filtering on cell content in a [reagent](https://github.com/reagent-project/reagent) webapp.

# Step 1: Create a new project

```
$ lein new rc filter-and-sort-table
```

# Step 2: Add necessary items to `resources/public/index.html`

We will add foundation for an easy way to make our example more visually pleasant while we experiment.

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.5.2/css/normalize.min.css" />
  <!-- Added style sheets -->
    <!-- Foundation CSS -->
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.5.2/css/foundation.min.css" />
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.5.2/css/foundation.min.css" />
    <!-- CSS -->
    <link rel="stylesheet" href="css/table.css">
  <!-- /Added style sheets -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

# Step 3: Build out the example in `src/cljs/filter_and_sort_table/core.cljs`

## Namespace and requires

For this example we will only need to require reagent itself and the reagent atom.

```clojure
(ns filter-and-sort-table.core
    (:require [reagent.core :as reagent]
              [reagent.core :as reagent :refer [atom]]))
```

## Atoms

We will use two atoms hold the values which define how our table is sorted and filtered

```clojure
(def select-value (atom "first-name"))
(def filter-value (atom ""))
```

## The table contents

We create a list of maps which contain the data that will be displayed in the table

```clojure
(def table-contents
  [{:id 1 :first-name "Bram"    :last-name "Moolenaar"  :known-for "Vim"}
   {:id 2 :first-name "Richard" :last-name "Stallman"   :known-for "GNU"}
   {:id 3 :first-name "Dennis"  :last-name "Ritchie"    :known-for "C"}
   {:id 4 :first-name "Rich"    :last-name "Hickey"     :known-for "Clojure"}
   {:id 5 :first-name "Guido"   :last-name "Van Rossum" :known-for "Python"}
   {:id 6 :first-name "Linus"   :last-name "Torvalds"   :known-for "Linux"}
   {:id 7 :first-name "Yehuda"  :last-name "Katz"       :known-for "Ember"}])
```

## Sort select box

The column the table is sorted on will be determined by a select box. We create
a select box with the desired options and update the value of the atom that is
passed in when when selectbox changes. When we call this function we will pass
in the `select-value` atom we created earlier.

```clojure
(defn table-select-box [value]
  [:select {:value     @value
            :on-change #(reset! value (-> % .-target .-value))}
   [:option {:value "first-name"} "First Name"]
   [:option {:value "last-name"}  "Last Name"]
   [:option {:value "known-for"}  "Known For"]])
```

## Filter input box

The contents of the table will be filtered by using an input box. We create an
input box what will display the value of the atom that is passed in and update
that atom when the value is changed.

```clojure
(defn table-filter-box [filter-val]
  [:input {:type        "text"
           :value       @filter-val
           :placeholder "Filter contents"
           :on-change   #(reset! filter-val (-> % .-target .-value))}])
```

## Filter the table contents

We only want to filter the table contents if the user has entered a value in
the filter box. First we check if there is a filter value. If there is not we
just return the contents unfiltered. If there is we check if any of the fields in
each row starts with the filter value and if so we will include it in the
table.

```clojure
(defn filtered-table-contents [contents filter-val]
  (if (empty? filter-val)
    contents
    (filter #(or (.startsWith (:first-name %) filter-val)
                 (.startsWith (:last-name  %) filter-val)
                 (.startsWith (:known-for  %) filter-val)) contents)))
```

## Filtered and sorted table contents

Now that we have the contents filtered we can sort them. Here we just have to
sort the filtered contents by the value of the select box.

```clojure
(defn filtered-and-sorted-contents [select-val filter-val contents]
  (sort-by (keyword @select-val)
    (filtered-table-contents contents @filter-val)))
```

## The table

At this point we have enough functions built up to compose the table. We create
the static head row and then create a body row for each map in our
`filtered-and-sorted-contents` by using a `for` list comprehension.

```clojure
(defn table [select-val filter-val contents]
  [:table
    [:thead
      [:tr [:th {:width "200"} "First Name"] [:th {:width "200"} "Last Name"] [:th {:width "200"} "Known For"]]]
    [:tbody
      (for [person (filtered-and-sorted-contents select-val filter-val contents)]
        ^{:key (:id person)} [:tr [:td (:first-name person)] [:td (:last-name person)] [:td (:known-for person)]])]])
```

## Putting the pieces together

All the hard work is done we just need to put the parts together in the way we
want them to be displayed.

```clojure
(defn table-page []
  [:div {:id "table-wrap"}
    [table-select-box select-value]
    [table-filter-box filter-value]
    [table select-value filter-value table-contents]])
```

## Render the component

Finally render our component.

```clojure
(reagent/render-component [table-page]
                          (.getElementById js/document "app"))
```

# Usage

Compile cljs files.

```
$ lein cljsbuild once
```

Start a server.

```
$ lein ring server
```
