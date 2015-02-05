# Problem

You want to add [sortable portlets](http://jqueryui.com/sortable/#portlets) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-sortable-portlets2.s3-website-us-west-1.amazonaws.com/)

We are going to follow this [example](http://jqueryui.com/sortable/#portlets).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create a few portlet element in `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`
7. Create `resources/public/css/screen.css` files and add necessary CSS
8. Add CSS file to `resources/public/index.html`

#### Step 1: Create a new project

```
$ lein new rc sortable-portlets
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION \/ -->
    <!-- jQuery UI -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Create a few portlet element in `home`

Navigate to `src/cljs/sortable_portlets/core.cljs`.  Let's create our portlets by adding some divs.  We need to add a few classes to our divs so jQuery can manipulate them and add the portlet functionality.

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
;; ATTENTION \/
   [:div.column
    [:div.portlet
     [:div.portlet-header "Feeds"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]]
    [:div.portlet
     [:div.portlet-header "News"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ]

   [:div.column
    [:div.portlet
     [:div.portlet-header "Shopping"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ] 

   [:div.column
    [:div.portlet
     [:div.portlet-header "Links"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]]
    [:div.portlet
     [:div.portlet-header "Images"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ]
;; ATTENTION /\
   ])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
  $(function() {
    $( ".column" ).sortable({
      connectWith: ".column",
      handle: ".portlet-header",
      cancel: ".portlet-toggle",
      placeholder: "portlet-placeholder ui-corner-all"
    });
 
    $( ".portlet" )
      .addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
      .find( ".portlet-header" )
        .addClass( "ui-widget-header ui-corner-all" )
        .prepend( "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>");
 
    $( ".portlet-toggle" ).click(function() {
      var icon = $( this );
      icon.toggleClass( "ui-icon-minusthick ui-icon-plusthick" );
      icon.closest( ".portlet" ).find( ".portlet-content" ).toggle();
    });
  });
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (js/$ (fn []
          (.sortable (js/$ ".column") (clj->js {:connectWith ".column"
                                                :handle ".portlet-header"
                                                :cancel ".portlet-toggle"
                                                :placeholder "portlet-placeholder ui-corner-all"}))
          (.. (js/$ ".portlet")
              (addClass "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
              (find ".portlet-header")
              (addClass "ui-widget-header ui-corner-all")
              (prepend "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>"))

          (.click (js/$ ".portlet-toggle") (fn []
                                             (this-as this 
                                                      (let [icon (js/$ this)]
                                                        (.toggleClass icon "ui-icon-minusthick ui-icon-plusthick")
                                                        (.toggle (.find (.closest icon ".portlet") ".portlet-content"))
                                                        )))))))
```

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:render home
                         :component-did-mount home-did-mount}))
```

#### Step 6: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
```

#### Step 7: Create `resources/public/css/screen.css` files and add necessary CSS

```css
body {
    min-width: 520px;
}
.column {
    width: 170px;
    float: left;
    padding-bottom: 100px;
}
.portlet {
    margin: 0 1em 1em 0;
    padding: 0.3em;
}
.portlet-header {
    padding: 0.2em 0.3em;
    margin-bottom: 0.5em;
    position: relative;
}
.portlet-toggle {
    position: absolute;
    top: 50%;
    right: 0;
    margin-top: -8px;
}
.portlet-content {
    padding: 0.4em;
}
.portlet-placeholder {
    border: 1px dotted black;
    margin: 0 1em 1em 0;
    height: 50px;
}
```

#### Step 8:  Add CSS file to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
    <!-- jQuery UI -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<!-- ATTENTION \/ -->
    <!-- CSS -->
    <link rel="stylesheet" href="/css/screen.css">
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
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
	
