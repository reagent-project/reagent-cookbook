(ns file-upload.core
  (:require
   [reagent.core :as reagent]
   [cljsjs.filestack]))


(defn file-picker []
  [:input
   {:type              "filepicker"
    ;; TODO: PUT YOUR API KEY HERE
    :data-fp-apikey    "AhTgLagciQByzXpFGRI0Az"
    :data-fp-mimetypes "*/*"
    :data-fp-container "modal"
    :data-fp-multiple  "true"}])

(defn home []
  [:div
   [file-picker]
   ])

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))
