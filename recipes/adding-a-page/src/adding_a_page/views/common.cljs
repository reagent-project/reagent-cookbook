(ns adding-a-page.views.common
  (:require  [adding-a-page.session :as session :refer [get-state]]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.page-header {:class-name "row"}
   ;; 4 column units
  [:div#title {:class-name "col-md-4"} "adding-a-page"]
   ;; 8 column units
   [:div {:class-name "col-md-8"}
    [:ul.nav.nav-pills 
     [:li {:class (active? (get-state :nav) "home")}  [:a {:href "#/"} [:span {:class-name "fa fa-home"} " Home"]]]
     [:li {:class (active? (get-state :nav) "about")} [:a {:href "#/about"} "About"]]

;; ATTENTION \/
     [:li {:class (active? (get-state :nav) "new")} [:a {:href "#/new-page"} "New Page"]]
;; ATTENTION /\

     ]]])
