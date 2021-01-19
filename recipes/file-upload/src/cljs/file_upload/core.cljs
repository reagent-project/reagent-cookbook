(ns file-upload.core
  (:require
   [reagent.core :as reagent]
   [reagent.dom :as rdom]
   [promesa.core :as p]))


(defn btn-upload-image []
  ;; TODO: update XXXX with your api key
  (let [client (.init js/filestack "XXXX")]
    [:button
     {:on-click (fn []
                  (-> (p/promise
                       (.pick client (clj->js {:accept   "image/*"
                                               :maxFiles 5})))
                      (p/then #(let [files-uploaded (.-filesUploaded %)
                                     file           (aget files-uploaded 0)
                                     file-url       (.-url file)]
                                 (js/console.log "URL of file:" file-url)))))}
     "Upload Image"]))

(defn home []
  [:div
   [btn-upload-image]
   ])

(defn ^:export main []
  (rdom/render [home]
               (.getElementById js/document "app")))
