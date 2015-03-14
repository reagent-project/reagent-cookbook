# Problem

You want to use [mermaid](https://github.com/knsv/mermaid) (a d3 charting/diagramming library from markdown-like syntax) in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-mermaid.s3-website-us-west-1.amazonaws.com/)

We are going to follow the example in mermaid's [README](https://github.com/knsv/mermaid/blob/master/README.md).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add div with the class of _mermaid_ to `home`, along with the text for a diagram
4. Call mermaid's `init` method in a *did-mount* function
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`

#### Step 1: Create a new project

```
$ lein new rc mermaid
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
<!-- ATTENTION 1 of 2 \/ -->
  <head>
    <meta charset="utf-8">
  </head>
<!-- ATTENTION 1 of 2 /\ -->
  <body>
    <div id="app"> Loading... </div>
<!-- ATTENTION 2 of 2 \/ -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/mermaid/0.3.3/mermaid.full.js"></script>
<!-- ATTENTION 2 of 2 /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Add div with the class of _mermaid_ to `home`, along with the text for a diagram

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
  [:div.mermaid "sequenceDiagram
    participant Alice
    participant Bob
    Alice->>John: Hello John, how are you?
    loop Healthcheck
        John->>John: Fight against hypochondria
    end
    Note right of John: Rational thoughts <br/>prevail...
    John-->>Alice: Great!
    John->>Bob: How about you?
    Bob-->>John: Jolly good!"]
   ])
```

#### Step 4: Call mermaid's `init` method in a *did-mount* function

```clojure
(defn home-did-mount []
  (.init js/mermaid))
```

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:reagent-render home
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
