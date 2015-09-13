# Problem

You want to use [highcharts](http://www.highcharts.com/) charts to display data in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

We are going to create a bar chart and follow this [example](http://jsfiddle.net/2ohatwcd/1/).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Create a `home-render` function with an empty div
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home-render` and `home-did-mount` to create a reagent component called `home`
6. Add externs

#### Step 1: Create a new project

```
$ lein new rc highcharts
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
    <div id="app"></div>

    <!-- ATTENTION 2 of 2 \/ -->
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
    <!-- Highcharts -->
    <script src="http://code.highcharts.com/highcharts.js"></script>
    <script src="http://code.highcharts.com/modules/exporting.js"></script>
    <!-- ATTENTION 2 of 2 /\ -->

    <script src="js/compiled/app.js"></script>
    <script>highcharts.core.main();</script>
  </body>
</html>
```

#### Step 3: Create a `home-render` function with an empty div

Navigate to `src/cljs/highcharts/core.cljs`.

```clojure
(defn home-render []
  [:div {:style {:min-width "310px" :max-width "800px" 
                 :height "400px" :margin "0 auto"}}])
```

#### Step 4: Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`

This is the javascript we need.

```javascript
$(function () {
    $('#container').highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Historic World Population by Region'
        },
        subtitle: {
            text: 'Source: Wikipedia.org'
        },
        xAxis: {
            categories: ['Africa', 'America', 'Asia', 'Europe', 'Oceania'],
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Population (millions)',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: ' millions'
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',    
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: 'Year 1800',
            data: [107, 31, 635, 203, 2]
        }, {
            name: 'Year 1900',
            data: [133, 156, 947, 408, 6]
        }, {
            name: 'Year 2008',
            data: [973, 914, 4054, 732, 34]
        }]
    });
});
```

Let's convert this to clojurescript and place in `home-did-mount`.  However, let's create a placeholder for the chart configurations called `chart-config` to make it more readable. Also, we can avoid the use of IDs by using reagent's `dom-node` function.

```clojure
(defn home-did-mount [this]
  (.highcharts (js/$ (reagent/dom-node this))
               (clj->js chart-config)))
```

Above `home-did-mount`, define `chart-config`.

```clojure
(def chart-config
  {:chart {:type "bar"}
   :title {:text "Historic World Population by Region"}
   :subtitle {:text "Source: Wikipedia.org"}
   :xAxis {:categories ["Africa" "America" "Asia" "Europe" "Oceania"]
           :title {:text nil}}
   :yAxis {:min 0
           :title {:text "Population (millions)"
                   :align "high"}
           :labels {:overflow "justify"}}
   :tooltip {:valueSuffix " millions"}
   :plotOptions {:bar {:dataLabels {:enabled true}}}
   :legend {:layout "vertical"
            :align "right"
            :verticalAlign "top"
            :x -40
            :y 100
            :floating true
            :borderWidth 1
            :shadow true}
   :credits {:enabled false}
   :series [{:name "Year 1800"
             :data [107 31 635 203 2]}
            {:name "Year 1900"
             :data [133 156 947 408 6]}
            {:name "Year 2008"
             :data [973 914 4054 732 34]}]
   })
```

#### Step 5: Use `home-render` and `home-did-mount` to create a reagent component called `home`

```clojure
(defn home []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))
```

#### Step 6: Add externs

For advanced compilation, we need to protect `$.highcharts` from getting renamed. Add an `externs.js` file.

```js
var $ = function(){};
$.highcharts = function(){};
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

