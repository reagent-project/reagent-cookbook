(ns page-from-markdown.util.elem)

(defn dangerous
  ([comp content]
     (dangerous comp nil content))
  ([comp props content]
     [comp (assoc props :dangerouslySetInnerHTML {:__html content})]))
