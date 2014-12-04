(ns adding-a-page.views.common
  (:require  [adding-a-page.session :as session :refer [global-state]]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.page-header.row

   [:div#title.col-md-6 
    "adding-a-page"]

   [:div.col-md-6
    [:ul.nav.nav-pills 
     [:li {:class (active? (global-state :nav) "home")}  [:a {:href "#/"} [:span [:i.fa.fa-home] " Home"]]]
     [:li {:class (active? (global-state :nav) "about")} [:a {:href "#/about"} "About"]]
     [:li {:class (active? (global-state :nav) "new")} [:a {:href "#/new"} "New Page"]]]]
   ])
