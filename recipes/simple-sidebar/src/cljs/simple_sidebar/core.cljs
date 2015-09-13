(ns simple-sidebar.core
    (:require [reagent.core :as reagent]))

(defn sidebar []
  [:div#sidebar-wrapper
   [:ul.sidebar-nav
    [:li.sidebar-brand>a {:href "#"} "Simple Sidebar"]
    [:li>a {:href "#"} "Page 1"]
    [:li>a {:href "#"} "Page 2"]
    [:li>a {:href "#"} "Page 3"]]])

(defn menu-toggle-render []
  [:div.btn.btn-default "Toggle Menu"])

(defn menu-toggle-did-mount [this]
  (.click (js/$ (reagent/dom-node this))
          (fn [e]
            (.preventDefault e)
            (.toggleClass (js/$ "#wrapper") "toggled") ;#wrapper will be the id of a div in our home component
            )))

(defn menu-toggle []
  (reagent/create-class {:reagent-render menu-toggle-render
                         :component-did-mount menu-toggle-did-mount}))

(defn home []
  [:div#wrapper
   [sidebar]
   [:div.page-content-wrapper>div.container-fluid>div.row>div.col-lg-12
    [menu-toggle]]])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))

