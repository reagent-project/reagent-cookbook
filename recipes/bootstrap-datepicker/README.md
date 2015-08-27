# Problem

You want to add [bootstrap-datepicker](https://github.com/eternicode/bootstrap-datepicker) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

[Demo](http://rc-bootstrap-datepicker2.s3-website-us-west-1.amazonaws.com/) | [Video](https://www.youtube.com/watch?v=kSzb8YHZV9Q)

We are going to follow this [example](http://runnable.com/UmOlOZbXvZRqAABU/bootstrap-datepicker-example-text-input-with-specifying-date-format2).

*Steps*

1. Create a new project
2. Add necessary itmes to `resources/public/index.html`
3. Add text input with unique id to `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`

#### Step 1: Create a new project

```
$ lein new rc bootstrap-datepicker
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
<!-- ATTENTION \/ -->
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- datepicker -->
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker3.min.css">
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.min.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Add text input with unique id to `home`

Navigate to `src/cljs/bootstrap_datepicker/core.cljs`. 

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:input#example1 {:type "text" :placeholder "click to show datepicker"}]
   ])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
$(document).ready(function () {
    $('#example1').datepicker({
        format: "dd/mm/yyyy"
    });
});
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"})))))
```

The `.ready` method is used to assure that the DOM node exists on the page before executing the `.datepicker` method. However, since we are tapping into the did-mount lifecycle of the component, we are already assured that the component will exist. Let's refactor the code as follows:

```clojure
(defn home-did-mount [this]
  (.datepicker (js/$ (js/$ "#example1")) (clj->js {:format "dd/mm/yyyy"})))
```

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:reagent-render home
                         :component-did-mount home-did-mount}))
```

#### Step 6: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render [home-component] (.getElementById js/document "app"))
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
