# Problem

You want to add [bootstrap-datepicker](https://github.com/eternicode/bootstrap-datepicker) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

[Demo](http://rc-bootstrap-datepicker2.s3-website-us-west-1.amazonaws.com/) | [Video](https://www.youtube.com/watch?v=kSzb8YHZV9Q)

We are going to follow this [example](http://runnable.com/UmOlOZbXvZRqAABU/bootstrap-datepicker-example-text-input-with-specifying-date-format2).

*Steps*

1. Create a new project
2. Add necessary itmes to `resources/public/index.html`
3. Add text input to `datepicker-render`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `datepicker-did-mount`
5. Use `datepicker-render` and `datepicker-did-mount` to create a reagent component called `datepicker`
6. Add the `datepicker` component to the `home` component

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

#### Step 3: Add text input to `datepicker-render`

Navigate to `src/cljs/bootstrap_datepicker/core.cljs`. 

```clojure
(defn datepicker-render []
  [:input {:type "text" :placeholder "click to show datepicker"}])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `datepicker-did-mount`

This is the javascript we need.

```javascript
$(document).ready(function () {
    $('#example1').datepicker({
        format: "dd/mm/yyyy"
    });
});
```

Let's convert this to clojurescript and place in `datepicker-did-mount`

```clojure
(defn datepicker-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#example1") (clj->js {:format "dd/mm/yyyy"})))))
```

The `.ready` method is used to assure that the DOM node exists on the page before executing the `.datepicker` method. However, since we are tapping into the did-mount lifecycle of the component, we are already assured that the component will exist. In addition, rather than using jQuery to select the element by id, we can get the DOM node directly using React/Reagent.  Let's refactor the code as follows:

```clojure
(defn datepicker-did-mount [this]
  (.datepicker (js/$ (reagent/dom-node this)) (clj->js {:format "dd/mm/yyyy"})))
```

#### Step 5: Use `datepicker-render` and `datepicker-did-mount` to create a reagent component called `datepicker`

```clojure
(defn datepicker []
  (reagent/create-class {:reagent-render datepicker-render
                         :component-did-mount datepicker-did-mount}))
```

#### Step 6: Add the `datepicker` component to the `home` component

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [datepicker]])
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
