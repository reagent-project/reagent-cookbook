# Problem

You want to add [nvd3](http://nvd3.org/) to your [reagent](https://github.com/reagent-project/reagent) web application.

# Solution

[Demo](http://rc-nvd3.s3-website-us-west-1.amazonaws.com/)

We are going to create a line chart and loosely follow this [example](http://nvd3.org/examples/line.html).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add parent div with inner svg element to `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`

#### Step 1: Create a new project

```
$ lein new rc nvd3
```

#### Step 2: Add necessary items to `resources/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
<!-- ATTENTION 1 of 2 \/ -->
  <head>
    <meta charset="utf-8">
  </head>
<!-- ATTENTION 1 of 2 /\ -->
  <body>
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION 2 of 2 \/ -->
    <!-- nvd3 -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.4.13/d3.js"></script>
    <link href="//cdnjs.cloudflare.com/ajax/libs/nvd3/1.1.15-beta/nv.d3.css" rel="stylesheet">
    <script src="//cdnjs.cloudflare.com/ajax/libs/nvd3/1.1.15-beta/nv.d3.js"></script>
<!-- ATTENTION 2 of 2 /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Add parent div with inner svg element to `home`

Navigate to `src/cljs/nvd3/core.cljs`.

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#d3-node {:style {:width "750" :height "420"}} [:svg ]]
   ])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

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

Let's convert this to clojurescript and place in `home-did-mount`

```clojure
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
```

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:render home
                         :component-did-mount home-did-mount}))
```

#### Step 6: Change the initially rendered component from `home` to `home-component`

```clojure
(reagent/render-component [home-component]
                          (.getElementById js/document "app"))
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
