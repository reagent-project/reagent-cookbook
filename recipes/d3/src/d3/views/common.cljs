(ns d3.views.common
  (:require  [d3.session :as session :refer [global-state]]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.page-header.row

  [:div#title.col-md-6 
   "d3"]

   [:div.col-md-6
    [:ul.nav.nav-pills 
     [:li {:class (active? (global-state :nav) "home")}  [:a {:href "#/"} [:span [:i.fa.fa-home] " Home"]]]
     [:li {:class (active? (global-state :nav) "about")} [:a {:href "#/about"} "About"]]]]
   ])
