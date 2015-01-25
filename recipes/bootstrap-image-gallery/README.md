# Problem

You want to use [bootstrap image gallery](https://blueimp.github.io/Bootstrap-Image-Gallery/) in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-bootstrap-image-gallery.s3-website-us-west-1.amazonaws.com/)

We are going to roughly follow the [example](https://github.com/blueimp/Bootstrap-Image-Gallery) on their github page.

*Steps*

1. Create a new project
2. Download bootstrap-image-gallery
3. Add necessary items to `resources/public/index.html`
4. Add pictures and thumbnails of fruit
5. Create a function to help display the pictures of fruit called `fruit`
6. Add links to the pictures of fruit in `home`

#### Step 1: Create a new project

```
$ lein new rc bootstrap-image-gallery
```

#### Step 2: Download bootstrap-image-gallery

* cd into the project you made in step 1 and make the following folder: `resources/public/vendor`
* Download the most recent version of bootstrap image gallery from [here](https://github.com/blueimp/Bootstrap-Image-Gallery/tags), and extract its contents.
* Place the `css`, `img`, and `js` folders of bootstrap image gallery in the `resources/public/vendor` folder that you just made.

#### Step 3: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
<!-- ATTENTION 1 of 2 \/ -->
  <head>
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="//blueimp.github.io/Gallery/css/blueimp-gallery.min.css">
    <link rel="stylesheet" href="vendor/css/bootstrap-image-gallery.min.css">
  </head>
<!-- ATTENTION 1 of 2 /\ -->
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>

<!-- ATTENTION 2 of 2 \/ -->
    <!-- The Bootstrap Image Gallery lightbox, should be a child element of the document body -->
    <div id="blueimp-gallery" class="blueimp-gallery">
      <!-- The container for the modal slides -->
      <div class="slides"></div>
      <!-- Controls for the borderless lightbox -->
      <h3 class="title"></h3>
      <a class="prev">‹</a>
      <a class="next">›</a>
      <a class="close">×</a>
      <a class="play-pause"></a>
      <ol class="indicator"></ol>
      <!-- The modal dialog, which will be used to wrap the lightbox content -->
      <div class="modal fade">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" aria-hidden="true">&times;</button>
              <h4 class="modal-title"></h4>
            </div>
            <div class="modal-body next"></div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default pull-left prev">
                <i class="glyphicon glyphicon-chevron-left"></i>
                Previous
              </button>
              <button type="button" class="btn btn-primary next">
                Next
                <i class="glyphicon glyphicon-chevron-right"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="//blueimp.github.io/Gallery/js/jquery.blueimp-gallery.min.js"></script>
    <script src="vendor/js/bootstrap-image-gallery.min.js"></script>
<!-- ATTENTION 2 of 2 /\ -->

    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 4: Add pictures and thumbnails of fruit

* Create the following folder: `resources/public/img`
* Find a picture of an orange, an apple, and a banana online. Save them to `resources/public/img`
* Create another folder: `resources/public/img/thumbnails`
* Create thumbnails of the fruit pictures that you found and save them to `resources/public/img/thumbnails`

#### Step 5: Create a function to help display the pictures of fruit called `fruit`

Navigate to `src/cljs/bootstrap_image_gallery/core.cljs`.

```clojure
(defn fruit [file-name file-type]
  [:a {:href (str "img/" file-name "." file-type) :title name :data-gallery ""}
   [:img {:src (str "img/thumbnails/" file-name "." file-type) :alt file-name}]])
```

#### Step 6: Add links to the pictures of fruit in `home`

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#links
    [fruit "banana" "jpg"]
    [fruit "apple" "jpg"]
    [fruit "orange" "jpg"]
    ]])
```

# Usage

Compile cljs files.

```
$ lein cljsbuild once
```

Start a server.

```
$ lein ring server
```
