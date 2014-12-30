(ns sortable-portlets.core
    (:require [reagent.core :as reagent]))

(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div.column
    [:div.portlet
     [:div.portlet-header "Feeds"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]]
    [:div.portlet
     [:div.portlet-header "News"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ]

   [:div.column
    [:div.portlet
     [:div.portlet-header "Shopping"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ] 

   [:div.column
    [:div.portlet
     [:div.portlet-header "Links"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]]
    [:div.portlet
     [:div.portlet-header "Images"]
     [:div.portlet-content "Lorem ipsum dolor sit amet, consectetuer adipiscing elit"]] ]
   ])

(defn home-did-mount []
  (js/$ (fn []
          (.sortable (js/$ ".column") (clj->js {:connectWith ".column"
                                                :handle ".portlet-header"
                                                :cancel ".portlet-toggle"
                                                :placeholder "portlet-placeholder ui-corner-all"}))
          (.. (js/$ ".portlet")
              (addClass "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
              (find ".portlet-header")
              (addClass "ui-widget-header ui-corner-all")
              (prepend "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>"))

          (.click (js/$ ".portlet-toggle") (fn []
                                             (this-as this 
                                                      (let [icon (js/$ this)]
                                                        (.toggleClass icon "ui-icon-minusthick ui-icon-plusthick")
                                                        (.toggle (.find (.closest icon ".portlet") ".portlet-content"))
                                                        )))))))

(defn home-component []
  (reagent/create-class {:render home
                         :component-did-mount home-did-mount}))

(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
