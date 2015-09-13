# Problem

You want to create a table that allows sorting of column content by clicking on the column
headers.

# Solution

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create a reagent atom called `app-state`
4. Add the table contents
5. Setup the sort value
6. Sort the table contents
7. Create the `table` component
8. Add `table` to `home`

#### Step 1: Create a new project

```
$ lein new rc sort-table
```

#### Step 2: Add necessary items to `resources/public/index.html`

We will add foundation for an easy way to make our example more visually
pleasant while we experiment. Modify `resources/public/index.html` to look like
the following:

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>

    <!-- ATTENTION \/ -->
    <!-- Foundation CSS -->
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.5.2/css/normalize.min.css" />
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.5.2/css/foundation.min.css" />
    <!-- ATTENTION /\ -->

    <script src="js/compiled/app.js"></script>
    <script>sort_table.core.main();</script>
  </body>
</html>
```

#### Step 3: Create a reagent atom called `app-state`

From here on out we will be working in `src/cljs/sort_table/core.cljs`

We store our components state in a reagent atom. Let's go ahead and create that next.

```clojure
(def app-state (reagent/atom {:sort-val :first-name :ascending true}))
```

We have two elements of state to keep track of. The `:sort-val` will specify
which column is currently being sorted, and `:ascending` will determine which
direction we should sort the contents in.

#### Step 4: Add the table contents

Next up we create a list of maps that contain the data that will be displayed in the table.

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

#### Step 5:  Setup the sort value

To start off with our table will be sorted on `:first-name` as declared in the
definition of the `app-state` reagent atom. We will need a way to change the sort column when
the user clicks on a heading. Clicking on a column that is already sorted will
reverse the sort direction. We will create a function `update-sort-value` that
will address this logic.

```clojure
(defn update-sort-value [new-val]
  (if (= new-val (:sort-val @app-state))
    (swap! app-state update-in [:ascending] not)
    (swap! app-state assoc :ascending true))
  (swap! app-state assoc :sort-val new-val))
```

#### Step 6: Sort the table contents

Now that we can decide which column to sort on we need to do the actual
sorting.

```clojure
(defn sorted-contents []
  (let [sorted-contents (sort-by (:sort-val @app-state) table-contents)]
    (if (:ascending @app-state)
      sorted-contents
      (rseq sorted-contents)))
```

#### Step 7: Create the `table` component

With our update and sort functions in place we can put the table together. We
assign the `update-sort-value` function to be called with the appropriate value
when the user clicks a heading and we use the `sorted-contents` to populate the
body of the table.

```clojure
(defn table []
  [:table
    [:thead
      [:tr
        [:th {:width "200" :on-click #(update-sort-value :first-name)} "First Name"]
        [:th {:width "200" :on-click #(update-sort-value :last-name) } "Last Name"]
        [:th {:width "200" :on-click #(update-sort-value :known-for) } "Known For"]]]
    [:tbody
      (for [person (sorted-contents)]
        ^{:key (:id person)} 
        [:tr [:td (:first-name person)] 
         [:td (:last-name person)] 
         [:td (:known-for person)]])]])
```

#### Step 8: Add `table` to `home`

At this point we have all the work done. All that is left is to get the component onto the page.

```clojure
(defn home []
  [:div {:style {:margin "auto"
                 :padding-top "30px"
                 :width "600px"}}
    [table]])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
