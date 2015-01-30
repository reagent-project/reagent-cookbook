(ns mermaid.core
    (:require [reagent.core :as reagent]))

(def example-diagram
"sequenceDiagram
    participant Alice
    participant Bob
    Alice->>John: Hello John, how are you?
    loop Healthcheck
        John->>John: Fight against hypochondria
    end
    Note right of John: Rational thoughts <br/>prevail...
    John-->>Alice: Great!
    John->>Bob: How about you?
    Bob-->>John: Jolly good!")

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div.mermaid example-diagram]
   ])

(reagent/render-component [home]
                          (.getElementById js/document "app"))
