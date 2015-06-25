# Problem

You want to create a live Markdown editor with code syntax highlighting in the preview.

# Solution

[Demo](https://rawgit.com/lacarmen/reagent-markdown-editor/master/demo/editor.html)

[Blog post](http://carmenla.me/blog/posts/2015-06-23-reagent-live-markdown-editor.html) about this project.

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Update `:cljsbuild` options in `project.clj` 
4. Create a local reagent atom in `page` and remove the `defonce`
5. Create the editor component and add it to the page
6. Create the preview and markdown components
7. Add the preview section to the right of the editor
8. Create a function to apply syntax highlighting the code blocks in the preview
9. Post-process the preview content using the `highlight-code` function
10. Add externs to your project (for production build)

#### Step 1: Create a new project

```
$ lein new reagent-figwheel markdown-editor
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.6/styles/default.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.1.1-1/css/united/bootstrap.min.css">
</head>
<body>
<div id="app"></div>
<script src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.6/highlight.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.6/languages/clojure.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/marked/0.3.2/marked.min.js"></script>
<!-- must be at the bottom -->
<script src="js/compiled/app.js"></script>
<script>markdown_editor.core.main();</script>
<!---->
</body>
</html>
```

#### Step 3: Update the `:cljsbuild` options in your `project.clj` to the following

```clojure
{:builds [{:source-paths ["src/cljs"]
           :figwheel     {:on-jsload "markdown-editor.core/main"}
           :compiler     {:main       markdown-editor.core
                          :output-to  "resources/public/js/compiled/app.js"
                          :output-dir "resources/public/js/compiled/out"
                          :asset-path "js/compiled/out"}}]}
```


#### Step 4: Create a local reagent atom in `page` and remove the `defonce`

Navigate to `src/cljs/markdown_editor/core.cljs`.

```clojure
(defn page []
  (let [content (reagent/atom nil)]
    (fn []
      [:div
       [:h1 "Live Markdown Editor"]])))
```

#### Step 5: Create the editor component and add it to the page

Binding the atom to the editor

```clojure
(defn editor [content]
  [:textarea.form-control
   {:value     @content
    :on-change #(reset! content (-> % .-target .-value))}])

(defn page []
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

#### Step 6: Create the preview and markdown components

We'll be using marked.js to compile the Markdown

```clojure
(defn markdown-component [content]
  (fn []
    [:div {:dangerouslySetInnerHTML
           {:__html (-> content str js/marked)}}]))

(defn preview [content]
  (when (not-empty @content)
    (markdown-component @content)))
```

#### Step 7: Add the preview section to the right of the editor

```clojure
(defn page []
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

#### Step 8: Create a function to apply syntax highlighting the code blocks in the preview

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

#### Step 9: Post-process the preview content using the `highlight-code` function

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

#### Step 10: Add externs to your project (for production build)

Create `/externs/syntax.js`. This is necessary if you will be building this code for production since the compiler will munge function names that belong to external libraries. 

```javascript
var hljs = {};
hljs.highlightBlock = function(){};
marked = function(){};
```

You'll need to specify this file in your `project.clj` in `:externs` and you'll also need to add `:optimizations :advanced`.

```clojure
{:builds [{:source-paths ["src/cljs"]
           :figwheel     {:on-jsload "reagent-markdown-preview.core/main"}
           :compiler     {:main          reagent-markdown-preview.core
                          :output-to     "resources/public/js/compiled/app.js"
                          :output-dir    "resources/public/js/compiled/out"
                          :asset-path    "js/compiled/out"
                          :optimizations :advanced
                          :externs       ["externs/syntax.js"]}}]}
```

# Usage

Running project in dev mode

```
lein figwheel
```

Build ClojureScript for production

```
lein clean
lein cljsbuild once
```
