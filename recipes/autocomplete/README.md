# Problem

You want to add [jQuery UI's](http://jqueryui.com/) autocomplete to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

[Demo](http://rc-autocomplete2.s3-website-us-west-1.amazonaws.com/)

We are going to follow this [example](http://jqueryui.com/autocomplete/).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add autocomplete element to `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`

#### Step 1: Create a new project

```
$ lein new rc autocomplete
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION \/ -->
    <script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Add autocomplete element to `home`

Navigate to `src/cljs/autocomplete/core.cljs`. This is the html we need.

```html
<div class="ui-widget">
  <label for="tags">Programming Languages: </label>
  <input id="tags">
</div>
```

Let's convert this and put in `home`.

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
;; ATTENTION \/
   [:div.ui-widget
    [:label {:for "tags"} "Programming Languages: "]
    [:input#tags]]
;; ATTENTION /\
   ])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
  $(function() {
    var availableTags = [
      "ActionScript",
      "AppleScript",
      "Asp",
      "BASIC",
      "C",
      "C++",
      "Clojure",
      "COBOL",
      "ColdFusion",
      "Erlang",
      "Fortran",
      "Groovy",
      "Haskell",
      "Java",
      "JavaScript",
      "Lisp",
      "Perl",
      "PHP",
      "Python",
      "Ruby",
      "Scala",
      "Scheme"
    ];
    $( "#tags" ).autocomplete({
      source: availableTags
    });
  });
```

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
(defn home-did-mount []
  (js/$ (fn []
          (let [available-tags ["ActionScript"
                                "AppleScript"
                                "Asp"
                                "BASIC"
                                "C"
                                "C++"
                                "Clojure"
                                "COBOL"
                                "ColdFusion"
                                "Erlang"
                                "Fortran"
                                "Groovy"
                                "Haskell"
                                "Java"
                                "JavaScript"
                                "Lisp"
                                "Perl"
                                "PHP"
                                "Python"
                                "Ruby"
                                "Scala"
                                "Scheme"]]
            (.autocomplete (js/$ "#tags") 
                           (clj->js {:source available-tags}))))))
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

# Usage

Compile cljs files.

```
$ lein cljsbuild once
```

Start a server.

```
$ lein ring server
```
