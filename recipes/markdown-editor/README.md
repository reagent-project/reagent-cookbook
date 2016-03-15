# Problem

You want to create a live Markdown editor with code syntax highlighting in the preview.

# Solution

[Demo](https://rawgit.com/lacarmen/reagent-markdown-editor/master/demo/editor.html)

[Blog post](http://carmenla.me/blog/posts/2015-06-23-reagent-live-markdown-editor.html) about this project.

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create a local reagent atom in `home`
4. Create the `editor` component and add it to `home`
5. Create the `markdown` component
6. Create the `preview` component and add it to `home`
7. Add externs to your project


#### Step 1: Create a new project

```
$ lein new rc markdown-editor
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
<!-- ATTENTION \/ 1 of 2 -->
  <head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.6/styles/default.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.1.1-1/css/united/bootstrap.min.css">
  </head>
<!-- ATTENTION /\ 1 of 2-->
  <body>
    <div id="app"></div>
<!-- ATTENTION \/ 2 of 2-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.6/highlight.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.6/languages/clojure.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/marked/0.3.2/marked.min.js"></script>
<!-- ATTENTION /\ 2 of 2-->
    <script src="js/compiled/app.js"></script>
    <script>markdown_editor.core.main();</script>
  </body>
</html>
```

#### Step 3: Create a local reagent atom in `home`

Navigate to `src/cljs/markdown_editor/core.cljs`.

```clojure
(defn home []
  (let [content (reagent/atom nil)]
    (fn []
      [:div
       [:h1 "Live Markdown Editor"]])))
```

#### Step 4: Create the `editor` component and add it to `home`

Binding the atom to the editor

```clojure
(defn editor [content]
  [:textarea.form-control
   {:value     @content
    :on-change #(reset! content (-> % .-target .-value))}])

(defn home []
  (let [content (reagent/atom nil)]
    (fn []
      [:div
       [:h1 "Live Markdown Editor"]
       ;; ATTENTION \/
       [:div.container-fluid
        [:div.row
         [:div.col-sm-6
          [:h3 "Editor"]
          [editor content]]]]
       ;; ATTENTION /\
       ])))
```

#### Step 5: Create the `markdown` component

First, let's create the render function that will convert the content
to markdown via marked.js.

```clojure
(defn markdown-render [content]
  [:div {:dangerouslySetInnerHTML
         {:__html (-> content str js/marked)}}])
```

Second, let's create the will-mount function, which will retrieve the
code blocks with a js query selector and use highlight.js for the
syntax highlighting.

```clojure
(defn highlight-code [html-node]
  (let [nodes (.querySelectorAll html-node "pre code")]
    (loop [i (.-length nodes)]
      (when-not (neg? i)
        (when-let [item (.item nodes i)]
          (.highlightBlock js/hljs item))
        (recur (dec i))))))

(defn markdown-did-mount [this]
  (let [node (reagent/dom-node this)]
    (highlight-code node)))
```

Finally, let's create the markdown-component.

```clojure
(defn markdown-component [content]
  (reagent/create-class
   {:reagent-render      markdown-render
    :component-did-mount markdown-did-mount}))
```

#### Step 6: Create the `preview` component and add it to `home`

Show a preview of the markdown.

```clojure
;; ATTENTION 1 of 2 \/
(defn preview [content]
  (when (not-empty @content)
    [markdown-component @content]))
;; ATTENTION 1 of 2 /\

(defn home []
  (let [content (reagent/atom nil)]
    (fn []
      [:div
       [:h1 "Live Markdown Editor"]
       [:div.container-fluid
        [:div.row
         [:div.col-sm-6
          [:h3 "Editor"]
          [editor content]]
         ;; ATTENTION 2 of 2\/
         [:div.col-sm-6
          [:h3 "Preview"]
          [preview content]]
         ;; ATTENTION 2 of 2/\
         ]]])))
```


#### Step 7: Add externs to your project

Create `externs.js`. This is necessary if you will be building this code for production since the compiler will munge function names that belong to external libraries. 

```javascript
var hljs = {};
hljs.highlightBlock = function(){};
var marked = function(){};
```

You'll need to specify this file in your `project.clj` in `:externs`.

```clojure
  :cljsbuild {:builds [{:id "prod"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :pretty-print false
;; ATTENTION \/
                                   :externs ["externs.js"]}}]}
;; ATTENTION /\
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
