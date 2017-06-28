# Problem

You want to be able to upload files.

# Solution

We are going to use the [filestack](https://www.filestack.com/) service.

*Steps*

1. Sign up and get a free api key
2. Create a new project
3. Add filestack to `resources/public/index.html`
4. Add promesa to `project.clj` :dependencies vector
5. Add promesa to `core.cljs` namespace
6. Create a button to upload an image, `btn-upload-image`
7. Add `btn-upload-image` to `home`
8. Add externs

#### Step 1: Sign up and get a free api key

Go to [filestack](https://www.filestack.com/) and create a free account. You will need to create a 'New application' to get an 'API Key'.

#### Step 2: Create a new project

```
$ lein new rc file-upload
```

#### Step 3: Add filestack to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"></div>

    <!-- ATTENTION \/ -->
    <script src="https://static.filestackapi.com/v3/filestack.js"></script>
    <!-- ATTENTION /\ -->

    <script src="js/compiled/app.js"></script>
    <script>droppable.core.main();</script>
  </body>
</html>
```

#### Step 4: Add promesa to `project.clj` :dependencies vector

```clojure
[funcool/promesa "1.8.1"]
```

#### Step 5: Add promesa to `core.cljs` namespace

Navigate to `src/cljs/file_upload/core.cljs` and update the `ns` to the following.

```clojure
(ns file-upload.core
  (:require
   [reagent.core :as reagent]
   [promesa.core :as p]))
```

#### Step 6: Create button to upload an image, `btn-upload-image`

The example pick code looks like this:

```html
var client = filestack.init('yourApiKey');
client.pick(pickerOptions);

client.pick({
  accept: 'image/*',
  maxFiles: 5
}).then(function(result) {
  console.log(JSON.stringify(result.filesUploaded))
})
```

Let's convert this to clojurescript - be sure to use the API Key that you got from step 1.

```clojure
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
```

#### Step 7: Add `btn-upload-image` to `home`

```clojure
(defn home []
  [:div
    [btn-upload-image]
   ])
```

#### Step 8: Add externs

For advanced compilation, we need to protect names used for interop from getting renamed. Add an `externs.js` file.

```js
var TopLevel = {
"filestack" : function () {},
"filesUploaded" : function () {},
"init" : function () {},
"log" : function () {},
"pick" : function () {},
"url" : function () {}
}
```

Open `project.clj` and add a reference to the externs in the cljsbuild portion.

```clojure
:externs ["externs.js"]
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.

Upload a couple files, then navigate to your app on filestack. You can see the uploaded files under the *Assets* section.
