# Problem

You want to be able to upload files.

# Solution

We are going to use the [filestack](https://www.filestack.com/) service.

*Steps*

1. Sign up and get a free api key
2. Create a new project
3. Add filestack to `project.clj` :dependencies vector
4. Add filestack to `core.cljs` namespace
5. Create a `file-picker` input
6. Add `file-picker` to `home`

#### Step 1: Sign up and get a free api key

Go to [filestack](https://www.filestack.com/) and create a free account. You will need to create a 'New application' to get an 'API Key'.

#### Step 2: Create a new project

```
$ lein new rc file-upload
```

#### Step 3: Add filestack to `project.clj` :dependencies vector

```clojure
[cljsjs/filestack "2.4.10-0"]
```

#### Step 4: Add filestack to `core.cljs` namespace

Navigate to `src/cljs/file_upload/core.cljs` and update the `ns` to the following.

```clojure
(ns file-upload.core
  (:require
   [reagent.core :as reagent]
   [cljsjs.filestack]))
```

#### Step 5: Create a `file-picker` input

The quickstart guide says to add this input:

```html
<input type="filepicker"
       data-fp-apikey="AhTgLagciQByzXpFGRI0Az"
       data-fp-mimetypes="*/*"
       data-fp-container="modal"
       data-fp-multiple="true"
       onchange="out='';for(var i=0;i<event.fpfiles.length;i++){out+=event.fpfiles[i].url;out+=' '};alert(out)">
```

Let's convert this to clojurescript - be sure to use the API Key that you got from step 1.

*Note: I am ignoring the onchange attribute, because it doesn't appear to matter ... please update this recipe if it does!*

```clojure
(defn file-picker []
  [:input
   {:type              "filepicker"
    ;; TODO: PUT YOUR API KEY HERE
    :data-fp-apikey    "AhTgLagciQByzXpFGRI0Az"
    :data-fp-mimetypes "*/*"
    :data-fp-container "modal"
    :data-fp-multiple  "true"}])
```

#### Step 6: Add `file-picker` to `home`

```clojure
(defn home []
  [:div
   [file-picker]
   ])
```

# Usage

Compile cljs files.

```
$ lein clean
$ lein cljsbuild once prod
```

Open `resources/public/index.html`.

Upload a couple files, then navigate to your app on filestack. You can see the uploaded files under the *Assets* section.
