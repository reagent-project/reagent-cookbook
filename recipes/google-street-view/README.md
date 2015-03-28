# Problem

You want to add Google Street View image to your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-google-street-view.s3-website-us-east-1.amazonaws.com/)

We are going to loosely follow this [example](https://www.udacity.com/course/viewer#!/c-ud110/l-3310298553/e-3180658599/m-3180658600) provided by [Udacity](https://www.udacity.com/).

*Steps*

1. Create a new project
2. Create the base part of the url for the street view image request
3. Create the address part of the url for the street view image request
4. Create a function that makes the full url for the street view image request
5. Create a reagent atom called `app-state` to store the street and city values
6. Create an `input` component where a user can change the value of a key in `app-state`
7. Create an `address` component that displays an image based on the street and city supplied by a user
8. Use the `address` componenet in the `home` page component

#### Step 1: Create a new project

```
$ lein new rc google-street-view
```

To request a google street view image with a url, you need to parts:

1. The "base" part
2. The address part

#### Step 2: Create the base part of the url for the street view image request

Navigate to `src/cljs/google_street_view/core.cljs`.

```clojure
(def base-url 
  "http://maps.googleapis.com/maps/api/streetview?size=600x400&location=")
```

#### Step 3: Create the address part of the url for the street view image request

```clojure
(defn address-url [street city]
  (str street ", " city))
```

#### Step 4: Create a function that makes the full url for the street view image request

```clojure
(defn street-view-url [street city]
  (str base-url 
       (address-url street city)))
```

#### Step 5: Create a reagent atom called `app-state` to store the street and city values

```clojure
(def app-state (reagent/atom {:street "24 Willie Mays Plaza" :city "San Francisco"}))
```

#### Step 6: Create an `input` component where a user can change the value of a key in `app-state`

```clojure
(defn input [k]
  [:input {:value (@app-state k)
           :on-change #(swap! app-state assoc k (-> % .-target .-value))}])
```

#### Step 7: Create an `address` component that displays an image based on the street and city supplied by a user

```clojure
(defn address []
  [:div
   [:p "Street: " [input :street]]
   [:p "City: " [input :city]] 
   [:img {:src (street-view-url (@app-state :street) (@app-state :city))}]
   ])
```

#### Step 8: Use the `address` componenet in the `home` page component

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [address]
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
