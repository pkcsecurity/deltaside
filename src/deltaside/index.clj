(ns deltaside.index
  (:require [hiccup.core :as html]
            [deltaside.css :as css]
            [deltaside.properties :as props]))

(def title "DELTASIDE")

(def app-js-path
  (if (= :dev (props/property :mode))
    "/development/index.js"
    "/release/index.js"))

(def scripts
  [app-js-path])

(def index
  (html/html
    {:mode :html}
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-16x16.png" :sizes "16x16"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-32x32.png" :sizes "32x32"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-96x96.png" :sizes "96x96"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-194x194.png" :sizes "194x194"}]
      [:title title]
      (map css/css css/csses)
      [:style css/styles]]
     [:body
      [:div#app
      (for [s scripts]
        [:script {:src s}])]]]))

(def index-404
  (html/html
    {:mode :html}
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-16x16.png" :sizes "16x16"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-32x32.png" :sizes "32x32"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-96x96.png" :sizes "96x96"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-194x194.png" :sizes "194x194"}]
      [:title title]
      (map css/css css/csses)
      [:style css/styles]]
     [:body
      [:div#app
       (for [s scripts]
         [:script {:src s}])
       [:h1 "Route not found!"]]]]))
