# Problem

You want to identify which argument(s) triggered the `:component-will-update` method in your [Reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

The `:component-will-update` handler provides the new values of the component's parameters via `new-argv`. 
Using the Reagent api, we can also extract the prior values of the component arguments. 
By comparing the new values with the old, we can identify which arguments triggered the update.

*Steps*

1. Create a new project
2. Add css to `resources/public/index.html`
3. Create a component which compares prior vs current arguments
4. Update the `home` component to have two atoms and buttons to increment each one

#### Step 1: Create a new project

```
$ lein new rc compare-argv
```

#### Step 2: Add css to `resources/public/index.html`

Add some simple css styling which will be used to highlight the most recently updated atom.
Add the following html:

```html
  <head>
    <style>
      .selectedRow {background-color:yellow}
    </style>
  </head>
```

#### Step 3: Create a component which compares prior vs current arguments     

Navigate to `src/cljs/compare_argv/core.cljs`. 
Create the following component. In the `component-will-update` function, we call `(reagent/argv this)` which 
will return the list of current values of the component arguments. These are compared with new-argv. Depending on 
which argument changed, the `last-updated` atom will be updated to reflect which changed.

```clojure
(defn update-highlighter
  "Displays two vals and highlights the most recently updated one"
  [val1 val2]
  (let [last-updated (reagent/atom 0)]
    (reagent/create-class
      {:component-will-update (fn [this new-argv]
                                (let [[_ old-v1 old-v2] (reagent/argv this)
                                      [_ new-v1 new-v2] new-argv]
                                  (reset! last-updated
                                          (cond
                                            (> new-v1 old-v1) 1
                                            (> new-v2 old-v2) 2
                                            :else 0))))
       :reagent-render        (fn [val1 val2]
                                [:div
                                 ; display the current value of each,
                                 ; and highlight which was last updated
                                 [:div (when (= 1 @last-updated) {:class "selectedRow"})
                                  (str "val1 = " val1)]
                                 [:div (when (= 2 @last-updated) {:class "selectedRow"})
                                  (str "val2 = " val2)]])})))
```

#### Step 4: Update the `home` component to have two atoms and buttons to increment each one


```clojure
(defn home []
  (let [v1 (reagent/atom 0)
        v2 (reagent/atom 0)]
    (fn []
      [:div
       [:button {:on-click #(swap! v1 inc)} "inc val1"]
       [:button {:on-click #(swap! v2 inc)} "inc val2"]
       [update-highlighter @v1 @v2]])))
```


# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
