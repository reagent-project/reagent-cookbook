(ns simple-sidebar.core
    (:require [reagent.core :as reagent]))

(defn sidebar []
  [:div#sidebar-wrapper
   [:ul.sidebar-nav
    [:li.sidebar-brand [:a {:href "#"} "Simple Sidebar"]]
    [:li [:a {:href "#"} "Page 1"]]
    [:li [:a {:href "#"} "Page 2"]]
    [:li [:a {:href "#"} "Page 3"]]
    ]])

;; ---
(defn menu-toggle-render []
  [:div#menu-toggle.btn.btn-default "Toggle Menu"])

(defn menu-toggle-did-mount []
  (.click (js/$ "#menu-toggle") 
          (fn [e]
            (.preventDefault e)
            (.toggleClass (js/$ "#wrapper") "toggled") ;#wrapper from div in home
            )))

(defn menu-toggle []
  (reagent/create-class {:component-function menu-toggle-render
                         :component-did-mount menu-toggle-did-mount}))
;; ---

(defn home []
  [:div#wrapper
   [sidebar]
   [:div.page-content-wrapper
    [:div.container-fluid
     [:div.row
      [:div.col-lg-12
     [:h1 "Welcome to Reagent Cookbook!"]
     [menu-toggle]
       ]]]]])

(reagent/render-component [home]
                          (.getElementById js/document "app"))
