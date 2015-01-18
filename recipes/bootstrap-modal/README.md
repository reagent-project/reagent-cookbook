# Problem

You want to add a [modal window](http://getbootstrap.com/javascript/) to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-bootstrap-modal.s3-website-us-west-1.amazonaws.com/)

We are going to use the [reagent-modals](https://github.com/Frozenlock/reagent-modals) library.

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add reagent-modals to `project.clj`
4. Add reagent-modals to `src/cljs/bootstrap_modal/core.cljs` namespace
5. Add modal-window to `home`
6. Create a button to bring up the modal window
7. Add `modal-window-button` to `home`

#### Step 1: Create a new project

```
$ lein new rc bootstrap-modal
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION \/ -->
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<!-- ATTENTION /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Add reagent-modals to `project.clj`

Add the following to the `:dependencies` vector.

```clojure
[org.clojars.frozenlock/reagent-modals "0.2.2"]
```

#### Step 4: Add reagent-modals to `src/cljs/bootstrap_modal/core.cljs` namespace

```clojure
(ns bootstrap-modal.core
    (:require [reagent.core :as reagent]
              [reagent-modals.modals :as reagent-modals]))
```

#### Step 5: Add modal-window to `home`

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [reagent-modals/modal-window]
   ])
```
#### Step 6: Create a button to bring up the modal window

```clojure
(defn modal-window-button []
  [:div.btn.btn-primary {:on-click #(reagent-modals/modal! [:div "some message to the user!"])} 
   "My Modal"])
```

#### Step 7: Add `modal-window-button` to `home`

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [reagent-modals/modal-window]
;; ATTNETION \/
   [modal-window-button]
;; ATTENTION /\
   ])
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
	
