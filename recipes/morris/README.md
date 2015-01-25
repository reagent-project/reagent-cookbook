# Problem

You want to use [morris](http://morrisjs.github.io/morris.js/) charts to display data in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-morris2.s3-website-us-west-1.amazonaws.com/)

We are going to create a donut chart and follow this [example](http://jsbin.com/ukaxod/144/embed?html,js,output).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add div with unique id to `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`

#### Step 1: Create a new project

```
$ lein new rc morris
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION \/ -->
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Morris.js -->
    <script src="http://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
    <script src="http://cdn.oesmith.co.uk/morris-0.4.1.min.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Add div with unique id to `home`

Navigate to `src/cljs/morris/core.cljs`.

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#donut-example ]
   ])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
Morris.Donut({
  element: 'donut-example',
  data: [
    {label: "Download Sales", value: 12},
    {label: "In-Store Sales", value: 30},
    {label: "Mail-Order Sales", value: 20}
  ]
});
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (.Donut js/Morris (clj->js {:element "donut-example"
                              :data [{:label "Download Sales" :value 12}
                                     {:label "In-Store Sales" :value 30}
                                     {:label "Mail-Order Sales" :value 20}]})))
```

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:component-function home
                         :component-did-mount home-did-mount}))
```

#### Step 6: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render-component [home-component]
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
