# Problem

You want to toggle the class of an element (without using jQuery) in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-toggle-class2.s3-website-us-west-1.amazonaws.com/) [Video](https://www.youtube.com/watch?v=WcMrLhW20zg)

We are going to store the class of an element in a reagent atom, and then toggle the class when a button is clicked.

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create a `toggle-class` function
4. Add button and local state to `home`

#### Step 1: Create a new project

```
$ lein new rc toggle-class
```

#### Step 2: Add necessary items to `resources/public/index.html`

Let's add Bootstrap so we can use their classes in our toggle button.

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION \/ -->
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
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

#### Step 4: Add button and local state to `home`

```
(defn home []
  (let [state (reagent/atom {:btn-class "btn btn-default"})]
    (fn []
      [:div [:h1 "Welcome to Reagent Cookbook!"]
       [:div {:class (@state :btn-class)
              :on-click #(toggle-class state :btn-class "btn btn-default" "btn btn-danger")}
        "Click me"]
       ])))
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
