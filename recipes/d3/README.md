# Problem

You want to use [d3](http://d3js.org/) to display data in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

**Plan of Action**

We are going to create a line chart and loosely follow this [example](http://nvd3.org/examples/line.html).

Steps:

* Create a new project using the [reagent-seed](https://github.com/gadfly361/reagent-seed) template.
* Add d3 files to index.html.
* Add d3 to `home-page` component.
* Convert javascript from the example to clojurescript.
    * Change `home-page` component to `home-render` function.
    * Place the converted javascript code into a `home-did-mount` function.
	* Create `home-page` component which uses the `home-render` and `home-did-mount` functions.

Affected files:

* `resources/index.html`
* `src/d3/views/home_page.cljs`

## Create a reagent project

```
$ lein new reagent-seed d3
```

## Add d3 file to index.html

Add d3 to your `resources/public/index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta content="utf-8" http-equiv="encoding">  
    <title>d3</title>
  </head>
  <body>

    <div id="app"> Loading... </div>

    <!-- ReactJS -->
    <script src="http://fb.me/react-0.11.1.js"></script>
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <!-- Font Awesome -->

<!-- ATTENTION \/ -->
    <!-- d3 -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.4.13/d3.js"></script>
    <link href="//cdnjs.cloudflare.com/ajax/libs/nvd3/1.1.15-beta/nv.d3.css" rel="stylesheet">
    <script src="//cdnjs.cloudflare.com/ajax/libs/nvd3/1.1.15-beta/nv.d3.js"></script>
<!-- ATTENTION /\ -->

    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <!-- CSS -->
    <link rel="stylesheet" href="css/screen.css">
    <!-- Clojurescript -->
    <script src="/js/app.js"></script>

  </body>
</html>
```

## Add d3 to home-page component

Navigate to `src/d3/views/home_page.cljs`.  To add d3, we need to add a parent div with an inner svg element.

```clojure
(ns d3.views.home-page)

(defn home-page []
  [:div
   [:h2 "Home Page"]

;; ATTENTION \/
   [:div#d3-node [:svg ]]
;; ATTENTION /\
   
   ])
```

## Convert javascript to clojurescript

This is the javascript we want to include:

```javascript
nv.addGraph(function() {
  var chart = nv.models.lineChart()
                .margin({left: 100})  //Adjust chart margins to give the x-axis some breathing room.
                .useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
                .transitionDuration(350)  //how fast do you want the lines to transition?
                .showLegend(true)       //Show the legend, allowing users to turn on/off line series.
                .showYAxis(true)        //Show the y-axis
                .showXAxis(true)        //Show the x-axis
  ;

  chart.xAxis     //Chart x-axis settings
      .axisLabel('x-axis')
      .tickFormat(d3.format(',r'));

  chart.yAxis     //Chart y-axis settings
      .axisLabel('y-axis')
      .tickFormat(d3.format('.02f'));

  var myData = [
	{
		values: [{x: 1, y: 5}, {x: 2, y: 3}, {x: 3, y: 4}, {x: 4, y: 1}, {x: 5, y: 2}],
		key: "my-red-line",
		color: "red"
	}];

  d3.select('#d3-node svg') //Select the <svg> element you want to render the chart in.
      .datum(myData)         //Populate the <svg> element with chart data...
      .call(chart);          //Finally, render the chart!

  return chart;
});
```

Let's convert this to clojurescript.

```clojure
(.addGraph js/nv (fn []
                   (let [chart (.. js/nv -models lineChart
                                   (margin #js {:left 100})
                                   (useInteractiveGuideline true)
                                   (transitionDuration 350)
                                   (showLegend true)
                                   (showYAxis true)
                                   (showXAxis true))]
                     (.. chart -xAxis 
                         (axisLabel "x-axis") 
                         (tickFormat (.format js/d3 ",r")))
                     (.. chart -yAxis 
                         (axisLabel "y-axis") 
                         (tickFormat (.format js/d3 ",r")))

                     (let [my-data [{:x 1 :y 5} {:x 2 :y 3} {:x 3 :y 4} {:x 4 :y 1} {:x 5 :y 2}]]

                       (.. js/d3 (select "#d3-node svg")
                           (datum (clj->js [{:values my-data
                                             :key "my-red-line"
                                             :color "red"
                                             }]))
                           (call chart))))))
```

### Change home-page component to home-render function

However, if we use the above code, it will fail. This is because when we change views and come back to this view, the code won't get re-run.  What we need to do is tap into the react/reagent component lifecycle. First, let's change the `home-page` component to `home-render` function.

```clojure
...
(defn home-render []
...
```

### Create did-mount function

Next, let's add our code to a *did-mount* function called `home-did-mount`.

```clojure
(ns d3.views.home-page)

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#d3-node [:svg ]]
   ])

;; ATTENTION \/
(defn home-did-mount []
  (.addGraph js/nv (fn []
                     (let [chart (.. js/nv -models lineChart
                                     (margin #js {:left 100})
                                     (useInteractiveGuideline true)
                                     (transitionDuration 350)
                                     (showLegend true)
                                     (showYAxis true)
                                     (showXAxis true))]
                       (.. chart -xAxis 
                           (axisLabel "x-axis") 
                           (tickFormat (.format js/d3 ",r")))
                       (.. chart -yAxis 
                           (axisLabel "y-axis") 
                           (tickFormat (.format js/d3 ",r")))

                       (let [my-data [{:x 1 :y 5} {:x 2 :y 3} {:x 3 :y 4} {:x 4 :y 1} {:x 5 :y 2}]]

                         (.. js/d3 (select "#d3-node svg")
                             (datum (clj->js [{:values my-data
                                               :key "my-red-line"
                                               :color "red"
                                               }]))
                             (call chart)))))))
;; ATTENTION /\
```

### Create home-page component

To make the `home-page` component we have to add *reagent* to our namespace.

```clojure
(ns d3.views.home-page
  (:require [reagent.core :as reagent]))
...
```

Ok, finally, let's create our `home-page` component by using the `home-render` and `home-did-mount` functions.

```clojure
(ns d3.views.home-page
  (:require [reagent.core :as reagent]))

(defn home-render []
  [:div
   [:h2 "Home Page"]
   [:div#d3-node [:svg ]]
   ])

(defn home-did-mount []
  (.addGraph js/nv (fn []
                     (let [chart (.. js/nv -models lineChart
                                     (margin #js {:left 100})
                                     (useInteractiveGuideline true)
                                     (transitionDuration 350)
                                     (showLegend true)
                                     (showYAxis true)
                                     (showXAxis true))]
                       (.. chart -xAxis 
                           (axisLabel "x-axis") 
                           (tickFormat (.format js/d3 ",r")))
                       (.. chart -yAxis 
                           (axisLabel "y-axis") 
                           (tickFormat (.format js/d3 ",r")))

                       (let [my-data [{:x 1 :y 5} {:x 2 :y 3} {:x 3 :y 4} {:x 4 :y 1} {:x 5 :y 2}]]

                         (.. js/d3 (select "#d3-node svg")
                             (datum (clj->js [{:values my-data
                                               :key "my-red-line"
                                               :color "red"
                                               }]))
                             (call chart)))))))

;; ATTENTION \/
(defn home-page []
  (reagent/create-class {:render home-render
                         :component-did-mount home-did-mount}))
;; ATTENTION /\
```

# Usage

To view our app, we need to perform the following steps:

Create a css file.

```
$ lein garden once
```

*Note: if it says "Successful", but you aren't able to type anything into the terminal, hit `Ctrl-c Ctrl-c`.*

Create a javascript file from your clojurescript files.

```
$ lein cljsbuild once
```

Start a repl and then start the server.

```
$ lein repl

user=> (run!)
```

Open a browser and go to *localhost:8080*. You should see your reagent application!
