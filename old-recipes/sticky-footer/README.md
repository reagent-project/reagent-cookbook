# Problem

You want to add a sticky footer using [Garden](https://github.com/noprompt/garden) to your [reagent](https://github.com/reagent-project/reagent) webapp.

*(Note: Garden is a library that let's you write css in clojure)*

# Solution

**Plan of Action**

Our solution will be based on this this CSS-TRICKS [example](http://css-tricks.com/snippets/css/sticky-footer/).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add footer to index.html
* Add CSS

Affected files:

* `resources/public/index.html`
* `src/stick_footer/css/screen.cljs`

## Create a reagent project

```
$ lein new reagent-seed sticky-footer
```

## Add Footer to index.html

In our `resources/public/index.html` file, we need to add a parent div to our app and a footer.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>sticky-footer</title>
  </head>
  <body>

<!-- ATTENTION \/ -->
    <div class="page-wrap">
      <div id="app"> Loading... </div>
    </div>
    <footer class="site-footer container">
      I'm the Sticky Footer.
    </footer>
<!-- ATTENTION /\ -->

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- Font Awesome -->
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <!-- CSS -->
    <link rel="stylesheet" href="css/screen.css">
    <!-- Clojurescript -->
    <script src="/js/app.js"></script>

  </body>
</html>
```

## Add CSS

The css we want to add looks like this:

```css
* {
  margin: 0;
}
html, body {
  height: 100%;
}
.page-wrap {
  min-height: 90%;
  /* equal to footer height */
  margin-bottom: -100px; 
}
.page-wrap:after {
  content: "";
  display: block;
}
.site-footer, .page-wrap:after {
  height: 100px; 
}
.site-footer {
  background: orange;
}
```

However, we want to write this in clojure using Garden instead.  We can do this by going to the `src/sticky_footer/css/screen.clj` file and updating it as follows:

```clojure
(defstyles screen
  ;; Coloring Title
  [:div#title {:font-size (em 3)
               :color (rgb 123 45 6)}]

;; ATTENTION \/
  ;; Sticky Footer
  [:* {:margin "0"}]
  [:html :body {:height "100%"}]
  [:.page-wrap {:min-height "90%"
                ;; equal to footer height
                :margin-bottom "-100px"}]
  [:.page-wrap:after {:content "\"\""    ;notice the escape characters
                     :display "block"}]
  [:.site-footer :.page-wrap:after {:height "100px"}]
  [:.site-footer {:background "orange"}]
;; ATTENTION /\
  )
```

# Usage

To view our app, we need to perform the following steps:

Create a css file.

```
$ lein garden once
```

*Note: if it says "Successful", but you aren't able to type anything into the terminal, hit `Ctrl-c Ctrl-c`.*

Create a javascript file from your clojurescript files.

```
$ lein cljsbuild once
```

Start a repl and then start the server.

```
$ lein repl

user=> (run!)
```

Open a browser and go to *localhost:8080*. You should see your reagent application!
