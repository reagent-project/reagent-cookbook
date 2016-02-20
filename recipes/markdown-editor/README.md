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
5. Create the `markdown` and `preview` components
6. Add the `preview` section to the right of the `editor`
7. Create a function to apply syntax highlighting the code blocks in the `preview`
8. Post-process the preview content using the `highlight-code` function
9. Add externs to your project

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

#### Step 5: Create the `markdown` and `preview` components

We'll be using [marked.js]() to compile the Markdown

```clojure
(defn markdown-component [content]
  (fn []
    [:div {:dangerouslySetInnerHTML
           {:__html (-> content str js/marked)}}]))

(defn preview [content]
  (when (not-empty @content)
    (markdown-component @content)))
```

#### Step 6: Add the `preview` section to the right of the `editor`

```clojure
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
         ;; ATTENTION \/
         [:div.col-sm-6
          [:h3 "Preview"]
          [preview content]]
         ;; ATTENTION /\
         ]]])))
```

#### Step 7: Create a function to apply syntax highlighting the code blocks in the preview

Retrieve the code blocks with a js query selector and use highlight.js for the syntax highlighting

```clojure
(defn highlight-code [html-node]
  (let [nodes (.querySelectorAll html-node "pre code")]
    (loop [i (.-length nodes)]
      (when-not (neg? i)
        (when-let [item (.item nodes i)]
          (.highlightBlock js/hljs item))
        (recur (dec i))))))
```

#### Step 8: Post-process the preview content using the `highlight-code` function

Use `with-meta` to add metadata to the `fn` that we wrote earlier. We want to add `highlight-code` to `:component-did-mount` so that it will be called *after* the HTML has been generated and the preview has been mounted in the browser DOM. 

```clojure
(defn markdown-component [content]
  [(with-meta
     (fn []
       [:div {:dangerouslySetInnerHTML
              {:__html (-> content str js/marked)}}])
     {:component-did-mount
      (fn [this]
        (let [node (reagent/dom-node this)]
          (highlight-code node)))})])
```

#### Step 9: Add externs to your project

Create `externs.js`. This is necessary if you will be building this code for production since the compiler will munge function names that belong to external libraries. 

```javascript
var hljs = {};
hljs.highlightBlock = function(){};
marked = function(){};
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
