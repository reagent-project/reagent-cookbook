# Problem

You want to edit a label.

# Solution

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create 'label-edit' component
4. Call the 'label-edit' component with the starting label name

#### Step 1: Create a new project

```
$ lein new rc editable-label
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>

    <!-- Attention \/ -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <!-- Attention /\ -->

    <div id="app"></div>
    <script src="js/compiled/app.js"></script>
    <script>editable_label.core.main();</script>
  </body>
</html>
```

From here on out we will be working in `src/cljs/filter_table/core.cljs`

#### Step 3: Create 'label-edit' component

Double-clicking will change the 'label' to a 'input.text' field, hitting 'ENTER'
or moving your mouse out of the element will save the inputed text and convert
the 'input.text' back into a 'label'

```clojure
(defn label-edit [item]
  (let [form            (reagent/atom item)
        edit?           (reagent/atom false)
        input-component (fn []
                          [:input.text
                           {:value          @form
                            :on-change      #(reset! form (-> % .-target .-value))
                            :on-key-press   (fn [e]
                                              (let [enter? (= 13 (.-charCode e))]
                                                (when enter?
                                                  (reset! edit? false)
                                                  (reset! form @form))))
                            :on-mouse-leave #(do (reset! edit? false)
                                                 (reset! form @form))}])]
    (fn [item]
      [:div
       (if-not @edit?
         [:label {:on-double-click #(reset! edit? true)} @form]
         [input-component])])))
```
#### Step 4: Call 'label-edit' component with the initial label name

```clojure
(defn home []
  [:div.container
   [:h1 "Editing Labels"]
   [label-edit (str "Double-click to edit")]
   [label-edit (str "Edit me!!")]])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.