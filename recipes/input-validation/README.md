# Problem

You want to create color-coded input validation in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-input-validation.s3-website-us-west-1.amazonaws.com/)

We are going to recreate this [om-cookbook recipe](https://github.com/om-cookbook/om-cookbook/tree/master/recipes/input-validation) in reagent.

*Steps*

1. Create a new project
2. Create an `input-valid?` predicate function
3. Create `color` function that changes whether or not the input is valid
4. Create a local reagent atom in `home`
5. Add an input field to `home`
6. Wrap the input field in a span with a background color using the `color` function

#### Step 1: Create a new project

```
$ lein new rc input-validation
```

#### Step 2: Create an `input-valid?` predicate function

```clojure
(defn input-valid?
  "Valid if input is less than 10 characters"
  [x]
  (> 10 (count x)))
```

#### Step 3: Create `color` function that changes whether or not the input is valid

```clojure
(defn color [input]
  (let [valid-color "green"
        invalid-color "red"]
    (if (input-valid? input) 
      valid-color invalid-color)))
```

#### Step 4: Create a local reagent atom in `home`

Navigate to `src/cljs/input_validation/core.cljs`.

```clojure
(defn home []
  (let [state (reagent/atom {:user-input "some value"})]
    (fn []
      [:div [:h1 "Welcome to Reagent Cookbook!"]

       ])))
```

#### Step 5: Add an input field to `home`

```clojure
(defn home []
  (let [state (reagent/atom {:user-input "some value"})]
    (fn []
      [:div [:h1 "Welcome to Reagent Cookbook!"]
;; ATTENTION \/
       [:input {:value (@state :user-input)
                :on-change #(swap! state assoc :user-input (-> % .-target .-value))
;; ATTENTION /\
                }]])))
```

#### Step 6: Wrap the input field in a span with a background color using the `color` function

```clojure
(defn home []
  (let [state (reagent/atom {:user-input "some value"})]
    (fn []
      [:div [:h1 "Welcome to Reagent Cookbook!"]
;; ATTENTION \/
       [:span {:style {:padding "20px"
                       :background-color (color (@state :user-input))}}
;; ATTENTION /\
        [:input {:value (@state :user-input)
                 :on-change #(swap! state assoc :user-input (-> % .-target .-value))
                 }]]])))
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

