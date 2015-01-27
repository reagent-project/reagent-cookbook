# Problem

You want to use [highcharts](http://www.highcharts.com/) charts to display data in your [reagent](https://github.com/reagent-project/reagent) webapp.

# Solution

[Demo](http://rc-highcharts.s3-website-us-west-1.amazonaws.com/)

We are going to create a bar chart and follow this [example](http://jsfiddle.net/2ohatwcd/1/).

*Steps*

1. Create a new project
2. Add necessary items to `resources/public/index.html`
3. Add div with unique id to `home`
4. Convert javascript to clojurescript and put inside a *did-mount* function called `home-did-mount`
5. Use `home` and `home-did-mount` to create a reagent component called `home-component`
6. Change the initially rendered component from `home` to `home-component`

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
    <div id="app"> Loading... </div>
    <script src="http://fb.me/react-0.11.2.js"></script>
<!-- ATTENTION 2 of 2 \/ -->
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
    <!-- Highcharts -->
    <script src="http://code.highcharts.com/highcharts.js"></script>
    <script src="http://code.highcharts.com/modules/exporting.js"></script>
<!-- ATTENTION 2 of 2 /\ -->
    <script src="/js/app.js"></script>
  </body>
</html>
```

#### Step 3: Add div with unique id to `home`

Navigate to `src/cljs/highcharts/core.cljs`.

```clojure
(defn home []
  [:div [:h1 "Welcome to Reagent Cookbook!"]
   [:div#example {:style {:min-width "310px" :max-width "800px" 
                          :height "400px" :margin "0 auto"}}]
   ])
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

Let's convert this to clojurescript and place in `home-did-mount`.  However, let's create a placeholder for the chart configurations called `chart-config` to make it more readable.

```clojure
(defn home-did-mount []
  (js/$ (fn []
          (.highcharts (js/$ "#example")
                       (clj->js chart-config)))))
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

#### Step 5: Use `home` and `home-did-mount` to create a reagent component called `home-component`

```clojure
(defn home-component []
  (reagent/create-class {:component-function home
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
