(ns canvas-fills-div.core
    (:require
     [reagent.dom :as rdom]
     [reagent.core :as reagent]))

(def window-width (reagent/atom nil))

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
        @window-width
        [:div.with-canvas
         [:canvas (if-let [ node @dom-node ]
                    {:width (.-clientWidth node)
                     :height (.-clientHeight node)})]])})))

(defn home []
  [div-with-canvas])

(defn on-window-resize [ evt ]
  (reset! window-width (.-innerWidth js/window)))

(defn ^:export main []
  (rdom [home]
                  (.getElementById js/document "app"))
  (.addEventListener js/window "resize" on-window-resize))

