# Problem

You want a canvas that fills a div in at least one dimension,
as the browser window is resized.

# Solution

We are going to use `create-class` and a Reagent `atom` to listen for
events necessary to ensure that the canvas within a div fills the
containing div. As the window changes size, 

*Steps*

1. Create a new project
2. Add CSS to `resources/public/index.html`
3. Add an atom and event handler for tracking the size of the window
4. Create a `div-with-canvas` component
5. Create a function to draw the contents of the canvas
6. Refer to the `div-with-canvas` component in the `home` definition.

#### Step 1: Create a new project

```
$ lein new rc canvas-fills-div
```

#### Step 2: Add CSS to `resources/public/index.html`

Add some simple css styling to get a div that fills the window. Most
of this is here to ensure that the `with-canvas` div fills all
available vertical space. If all you need is a div/canvas that fills
the width, all but the `display: block` can be removed.


```html
<head>
  <style>
    html, body {
      margin:0;
      padding:0;
      height:100%;
    }

    div#app {
        height: 100%;
    }

    div.with-canvas {
        margin: 0px;
        padding: 0px;
        border: 0px;

        height: 100%;
    }

    div.with-canvas canvas {
        display: block;
    }
  </style>
</head>
```

#### Step 3: Add an atom and event handler for tracking the size of the window

Define a Reagent atom to track the current width of the window.  The
canvas component will refer to this atom so that it's re-rendered as the
window size changes.

```clojure
(def window-width (reagent/atom nil))
```

Define an event handler to update `window-width` when the window
resizes. The size of the canvas is taken from the enclosing div, so
the main point of this is that the atom is updated rather than the
specific value.

```clojure
(defn on-window-resize [ evt ]
  (reset! window-width (.-innerWidth js/window)))
```

Add `on-window-resize` as a window event listener for resize events.

```clojure
(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app"))
  (.addEventListener js/window "resize" on-window-resize))
```

#### Step 4: Create a `div-with-canvas` component

Navigate to `src/cljs/div_fills_canvasc/core.cljs`. This uses
`create-class` because we need to capture the DOM node at the time the
component is mounted as well as render into the canvas when the
component updates.

```clojure
(defn div-with-canvas [ ]
  (let [dom-node (reagent/atom nil)]
    (reagent/create-class
     {:component-did-update
      (fn [ this ]
        (draw-canvas-contents (.-firstChild @dom-node)))

      :component-did-mount
      (fn [ this ]
        (reset! dom-node (reagent/dom-node this)))

      :reagent-render
      (fn [ ]
        @window-width ;; Trigger re-render on window resizes
        [:div.with-canvas
         ;; reagent-render is called before the compoment mounts, so
         ;; protect against the null dom-node that occurs on the first
         ;; render
         [:canvas (if-let [ node @dom-node ]
                    {:width (.-clientWidth node)
                     :height (.-clientHeight node)})]])})))
```

#### Step 5: Create a function to draw the contents of the canvas

```clojure
(defn draw-canvas-contents [ canvas ]
  (let [ ctx (.getContext canvas "2d")
        w (.-clientWidth canvas)
        h (.-clientHeight canvas)]
    (.beginPath ctx)
    (.moveTo ctx 0 0)
    (.lineTo ctx w h)
    (.moveTo ctx w 0)
    (.lineTo ctx 0 h)
    (.stroke ctx)))
```

#### Step 6: Refer to the `div-with-canvas` component in the `home` definition.

```clojure
(defn home []
  [div-with-canvas])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.
