(ns markdown-editor.core
  (:require [reagent.core :as reagent]))

(defn editor [content]
  [:textarea.form-control
   {:value     @content
    :on-change #(reset! content (-> % .-target .-value))}])

(defn highlight-code [html-node]
  (let [nodes (.querySelectorAll html-node "pre code")]
    (loop [i (.-length nodes)]
      (when-not (neg? i)
        (when-let [item (.item nodes i)]
          (.highlightBlock js/hljs item))
        (recur (dec i))))))

(defn markdown-component [content]
  [(with-meta
     (fn []
       [:div {:dangerouslySetInnerHTML
              {:__html (-> content str js/marked)}}])
     {:component-did-mount
      (fn [this]
        (let [node (reagent/dom-node this)]
          (highlight-code node)))})])

(defn preview [content]
  (when (not-empty @content)
    (markdown-component @content)))

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
         [:div.col-sm-6
          [:h3 "Preview"]
          [preview content]]
         ]]])))


(defn ^:export main []
  (reagent/render [page]
                  (.getElementById js/document "app")))
