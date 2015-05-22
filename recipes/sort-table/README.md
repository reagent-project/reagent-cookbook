# Problem

You want to create a table that allows sorting of column content by clicking on the column
headers.

# Solution

[Demo](http://rc-sort-table.s3-website-us-west-1.amazonaws.com/)

## Step 1: Create a new project

```shell
lein new rc sort-table
```

## Step 2: Add needed items to index.html

We will add foundation for an easy way to make our example more visually
pleasant while we experiment. Modify `resources/public/index.html` to look like
the following:

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
  <!-- ATTENTION \/ -->
    <!-- Foundation CSS -->
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.5.2/css/normalize.min.css" />
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.5.2/css/foundation.min.css" />
    <!-- CSS -->
    <link rel="stylesheet" href="css/table.css">
  <!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

## Step 3: Add table.css

We are going to add a minimal css file to center the table. Create
`resources/public/css/table.css` and add the following contents:

```css
#table-wrap {
    margin: auto;
    padding-top: 30px;
    width: 600px;
}
```

## Step 4: Build out the example

From here on out we will be working in `src/cljs/sort_table/core.cljs`

### Atom

We store our components state in an atom. Let's go ahead and create that next.

```clojure
(def state (reagent/atom {:sort-val :first-name :ascending true}))
```

We have two elements of state to keep track of. The `:sort-val` will specify
which column is currently being sorted, and `:ascending` will determine which
direction we should sort the contents in.

### The table contents

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

### Setting the sort value

To start off with our table will be sorted on `:first-name` as declared in the
definition of the state atom. We will need a way to change the sort column when
the user clicks on a heading. Clicking on a column that is already sorted will
reverse the sort direction. We will create a function `update-sort-value` that
will address this logic.

```clojure
(defn update-sort-value [new-val]
  (if (= new-val (:sort-val @state))
    (swap! state assoc :ascending (not (:ascending @state)))
    (swap! state assoc :ascending true))
  (swap! state assoc :sort-val new-val))
```

### Sort the table contents

Now that we can decide which column to sort on we need to do the actual
sorting.

```clojure
(defn sorted-contents []
  (let [sorted-contents (sort-by (:sort-val @state) table-contents)]
    (if (:ascending @state)
      sorted-contents
      (rseq sorted-contents))))
```

### Assemble the table

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
        ^{:key (:id person)} [:tr [:td (:first-name person)] [:td (:last-name person)] [:td (:known-for person)]])]])
```

### Define and render the component

At this point we have all the work done. All that is left is to get the component onto the page.

```clojure
(defn table-component []
  [:div {:id "table-wrap"}
    [table]])

(reagent/render-component [table-component]
                          (.getElementById js/document "app"))
```

## Usage

Compile cljs files.

```
$ lein cljsbuild once
```

Stare a local server

```
$ lein ring server
```
