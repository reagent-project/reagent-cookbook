# Problem

You want to include a bootstrap-themed sidebar in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-simple-sidebar.s3-website-us-west-1.amazonaws.com/)

We are going to use [simple-sidebar](http://startbootstrap.com/template-overviews/simple-sidebar/).

*Steps*

1. Create a new project
2. Download Simple Sidebar CSS
3. Add necessary items to `resources/public/index.html`
4. Create a `sidebar` component
5. Create a `menu-toggle` button
6. Add `sidebar` and `menu-toggle` to `home`

#### Step 1: Create a new project

```
$ lein new rc simple-sidebar
```

#### Step 2: Download Simple Sidebar CSS

* Open your new project and create the following folder `resources/public/css`
* Go [here](http://startbootstrap.com/template-overviews/simple-sidebar/) and hit the "Download" button
* Extract the zip file and navigate to the `css` folder
* Copy `simple-sidebar.css` and place it into your project in the `resources/public/css` folder that you just made earlier

#### Step 3: Add necessary items to `resources/public/index.html`

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
    <!-- CSS -->
    <link rel="stylesheet" href="css/simple-sidebar.css">
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 4: Create a `sidebar` component

Navigate to `src/cljs/simple_sidebar/core.cljs`.

```clojure
(defn sidebar []
  [:div#sidebar-wrapper
   [:ul.sidebar-nav
    [:li.sidebar-brand [:a {:href "#"} "Simple Sidebar"]]
    [:li [:a {:href "#"} "Page 1"]]
    [:li [:a {:href "#"} "Page 2"]]
    [:li [:a {:href "#"} "Page 3"]]
    ]])
```

The classes `sidebar-wrapper` and `sidebar-nav` are from Simple Sidebar.  You can add real links in the href, but for now we are just going to use the `#` as a placeholder.

#### Step 5: Create a menu toggle button

```clojure
(defn menu-toggle-render []
  [:div#menu-toggle.btn.btn-default "Toggle Menu"])

(defn menu-toggle-did-mount []
  (.click (js/$ "#menu-toggle") 
          (fn [e]
            (.preventDefault e)
            (.toggleClass (js/$ "#wrapper") "toggled") ;#wrapper will be the id of a div in our home component
            )))

(defn menu-toggle []
  (reagent/create-class {:render menu-toggle-render
                         :component-did-mount menu-toggle-did-mount}))
```

For your reference, this is the javascript we are converting to clojurescript in `menu-toggle-did-mount`.

```javascript
$("#menu-toggle").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});
```

#### Step 6: Add `sidebar` and `menu-toggle` to `home`

```clojure
(defn home []
  [:div#wrapper
   [sidebar]
   [:div.page-content-wrapper
    [:div.container-fluid
     [:h1 "Welcome to Reagent Cookbook!"]
     [menu-toggle]
     ]]])
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
