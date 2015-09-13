# Problem

You want to add [jQuery UI's](http://jqueryui.com/) autocomplete to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

We are going to follow this [example](http://jqueryui.com/autocomplete/).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add autocomplete element to `home-render`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home`

#### Step 1: Create a new project

```
$ lein new rc autocomplete
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>

    <!-- ATTENTION \/ -->
    <script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.min.css">
    <script src="http://code.jquery.com/ui/1.11.2/jquery-ui.min.js"></script>
    <!-- ATTENTION /\ -->

    <script src="js/compiled/app.js"></script>
    <script>autocomplete.core.main();</script>
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

Let's convert this and put in a function called `home-render`.

```clojure
(defn home-render []
  [:div.ui-widget
   [:label {:for "tags"} "Programming Languages: "]
   [:input#tags]])
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
(def tags 
  ["ActionScript"
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
   "Scheme"])

(defn home-did-mount []
  (js/$ (fn []
          (.autocomplete (js/$ "#tags") 
                         (clj->js {:source tags})))))
```

#### Step 5: Use `home-render` and `home-did-mount` to create a reagent component called `home`

```clojure
(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
