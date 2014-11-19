(ns modals.views.common
  (:require  [modals.session :as session :refer [get-state]]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.page-header {:class-name "row"}
   ;; 4 column units
  [:div#title {:class-name "col-md-4"} "modals"]
   ;; 8 column units
   [:div {:class-name "col-md-8"}
    [:ul.nav.nav-pills 
     [:li {:class (active? (get-state :nav) "home")}  [:a {:href "#/"} [:span {:class-name "fa fa-home"} " Home"]]]
     [:li {:class (active? (get-state :nav) "about")} [:a {:href "#/about"} "About"]]
     ]]])
