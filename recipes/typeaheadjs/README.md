# Problem

You want to add [typeahead.js](https://twitter.github.io/typeahead.js/) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

We are going to follow this [example](https://twitter.github.io/typeahead.js/examples/).

*Steps*

1. Create a new project
2. Add necessary dependencies
3. Add necessary items to `resources/public/index.html`
4. Create the typeahead component
5. Add typeahead element to `home-render`


#### Step 1: Create a new project

```
$ lein new rc typeahead
```

#### Step 2: Add necessary dependencies

These dependnecies will provide the necessary externs for the advanced compilation:

```clojure
[cljsjs/jquery "2.2.2-0"]
[cljsjs/typeahead-bundle "0.11.1-1"]
```

#### Step 3: Add necessary items to `resources/public/index.html`

```html
<script src="https://code.jquery.com/jquery-3.0.0.min.js"></script>
    <script src="https://twitter.github.io/typeahead.js/releases/latest/typeahead.bundle.js"></script>
```

#### Step 4: Create a typeahead element

Navigate to `src/cljs/autocomplete/core.cljs`.

We'll start by creating a vector of states and the matcher function:

```clojure
(def states
  ["Alabama" "Alaska" "Arizona" "Arkansas" "California"
   "Colorado" "Connecticut" "Delaware" "Florida" "Georgia" "Hawaii"
   "Idaho" "Illinois" "Indiana" "Iowa" "Kansas" "Kentucky" "Louisiana"
   "Maine" "Maryland" "Massachusetts" "Michigan" "Minnesota"
   "Mississippi" "Missouri" "Montana" "Nebraska" "Nevada" "New Hampshire"
   "New Jersey" "New Mexico" "New York" "North Carolina" "North Dakota"
   "Ohio" "Oklahoma" "Oregon" "Pennsylvania" "Rhode Island"
   "South Carolina" "South Dakota" "Tennessee" "Texas" "Utah" "Vermont"
   "Virginia" "Washington" "West Virginia" "Wisconsin" "Wyoming"])
   
(defn matcher [strs]
  (fn [text callback]
    (->> strs
         (filter #(s/includes? % text))
         (clj->js)
         (callback))))
```

Next, we'll write the wrapper for the typeahead component. This will be called when the typeahead is mounted in the DOM:

```clojure
(defn typeahead-mounted [this]
  (.typeahead (js/$ (reagent/dom-node this))
              (clj->js {:hint true
                        :highlight true
                        :minLength 1})
              (clj->js {:name "states"
                        :source (matcher states)})))
```

We'll now add an atom to hold the state of the typeahead and the *render* function:

```clojure
(def typeahead-value (reagent/atom nil))

(defn render-typeahead []
  [:input.typeahead
   {:type :text
    :on-select #(reset! typeahead-value (-> % .-target .-value))
    :placeholder "Programming Languages"}])
```

Finally, we'll write the typeahead component:

```clojure
(defn typeahead []
  (reagent/create-class
    {:component-did-mount typeahead-mounted
     :reagent-render render-typeahead}))
```

#### Step 5: Add typeahead element to `home-render`

```clojure
(defn home []
  [:div.ui-widget
   (when-let [language @typeahead-value]
     [:label "selected: " language])
   [typeahead]])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
