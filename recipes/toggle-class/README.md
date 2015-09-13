# Problem

You want to toggle the class of an element (without using jQuery) in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to store the class of an element in a reagent atom, and then toggle the class when a button is clicked.

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create a `toggle-class` function
4. Create [form-2](https://github.com/Day8/re-frame/wiki/Creating-Reagent-Components#form-2--a-function-returning-a-function) component with a reagent atom called `local-state`
5. Add a button to `home` and uses the `toggle-class` function on-click

#### Step 1: Create a new project

```
$ lein new rc toggle-class
```

#### Step 2: Add necessary items to `resources/public/index.html`

Let's add Bootstrap css so we can use their button classes in our toggle button.

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>
    <script src="js/compiled/app.js"></script>

    <!-- ATTENTION \/ -->
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <!-- ATTENTION /\ -->

    <script>toggle_class.core.main();</script>
  </body>
</html>
```

#### Step 3: Create a `toggle-class` function

Navigate to `src/cljs/toggle_class/core.cljs`.

```clojure
(defn toggle-class [a k class1 class2]
  (if (= (@a k) class1)
    (swap! a assoc k class2)
    (swap! a assoc k class1)))
```

#### Step 4: Create a [form-2](https://github.com/Day8/re-frame/wiki/Creating-Reagent-Components#form-2--a-function-returning-a-function) component with a reagent atom called `local-state`

```clojure
(defn home []
  (let [local-state (reagent/atom {:btn-class "btn btn-default"})]
    (fn []
      [:div ])))
```

#### Step 5: Add a button to `home` and uses the `toggle-class` function on-click

```clojure
(defn home []
  (let [local-state (reagent/atom {:btn-class "btn btn-default"})]
    (fn []
      ;; ATTENTION \/
      [:div {:class (@local-state :btn-class)
             :on-click #(toggle-class local-state :btn-class "btn btn-default" "btn btn-danger")}
       "Click me"]
      ;; ATTENTION /\
      )))
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
