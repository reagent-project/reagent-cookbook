(ns markdown-editor.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as reagent]))


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

(defn markdown-render [content]
  [:div {:dangerouslySetInnerHTML
         {:__html (-> content str js/marked)}}])

(defn markdown-did-mount [this]
  (let [node (rdom/dom-node this)]
    (highlight-code node)))

(defn markdown-component [content]
  (reagent/create-class
   {:reagent-render      markdown-render
    :component-did-mount markdown-did-mount}))

(defn preview [content]
  (when (not-empty @content)
    [markdown-component @content]))

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

         [:div.col-sm-6
          [:h3 "Preview"]
          [preview content]]

         ]]])))

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))
