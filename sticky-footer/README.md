# Problem

You want to add a sticky footer using [Garden](https://github.com/noprompt/garden) to your [reagent](https://github.com/holmsand/reagent) webapp.

*(Note: Garden is a library that let's you write css in clojure)*

# Solution

Our solution will be based on this this [CSS-TRICKS page](http://css-tricks.com/snippets/css/sticky-footer/).

## Create a reagent project

Let's start off using the reagent-seed template since it comes with garden.

*(Note: this recipe was made when reagent-seed was version 0.1.5)*

```
$ lein new reagent-seed sticky-footer
```

## Familiarize yourself with directory layout

Now, let's briefly take a look at the directory layout of our reagent webapp.

```
dev/
    user.clj                --> functions to start server and browser repl (brepl)
    user.cljs               --> enabling printing to browser's console when connected through a brepl

project.clj                 --> application summary and setup

resources/
    index.html              --> this is the html for your application
    public/                 --> this is where assets for your application will be stored

src/example/
    core.cljs               ---> main reagent component for application
    css/
        screen.clj          ---> main css file using Garden
    routes.cljs             ---> defining routes using Secretary
    session.cljs            ---> contains atom with application state
    views/
        about_page.cljs     ---> reagent component for the about page
    	common.cljs         ---> common reagent components to all page views
    	home_page.cljs      ---> reagent component for the home page
    	pages.cljs          ---> map of page names to their react/reagent components
```

## Add Footer to index.html

In our `rescources/index.html` file, we need to add a parent div to our app and a footer.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>sticky-footer</title>
  </head>
  <body class="container">

    <div class="page-wrap">
      <div id="app"> Loading... </div>
    </div>
    <footer class="site-footer">
      I'm the Sticky Footer.
    </footer>
...
```

## Adding CSS

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
  )
```

## Start your Reagent App

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
