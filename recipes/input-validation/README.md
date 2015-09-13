# Problem

You want to create color-coded input validation in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to validate that a password is at least 6 characters long.

*Steps*

1. Create a new project
2. Create an `password-valid?` predicate function
3. Create `password-color` function that changes whether or not the password is valid
4. Create a reagent atom, `app-state`, to store the password
5. Create a `password` component that updates `app-state` on-change
6. Create a `home` component and add the `password` component inside of it

#### Step 1: Create a new project

```
$ lein new rc input-validation
```

#### Step 2: Create a `password-valid?` predicate function

```clojure
(defn password-valid?
  "Valid if password is greater than 5 characters"
  [password]
  (> (count password) 5))
```

#### Step 3: Create `password-color` function that changes whether or not the input is valid

```clojure
(defn password-color [password]
  (let [valid-color "green"
        invalid-color "red"]
    (if (password-valid? password) 
      valid-color 
      invalid-color)))
```

#### Step 4: Create a reagent atom, `app-state`, to store the password

```clojure
(def app-state (reagent/atom {:password nil}))
```

#### Step 5: Create a `password` component that updates `app-state` on-change

```clojure
(defn password []
  [:input {:type "password"
           :on-change #(swap! app-state assoc :password (-> % .-target .-value))}])
```

#### Step 6: Create a `home` component and add the `password` component inside of it

Add the `password` component and wrap it in a span. Give the span a border-color based on the password length by using `password-color`.

```clojure
(defn home []
  [:div {:style {:margin-top "30px"}}
   "Please enter a password greater than 5 characters. "
   [:span {:style {:padding "20px"
                   :background-color (password-color (@app-state :password))}}
    [password]
    ]])
```
