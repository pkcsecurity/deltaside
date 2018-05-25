(ns deltaside.cljs.core
  (:require [reagent.core :as r]
            [goog.dom :as dom]
            [deltaside.cljs.xhr :as xhr]
            [deltaside.cljs.model.global :as global]
            [deltaside.cljs.router :as router]))

(enable-console-print!)

(defn body []
  [:div
   ;[layout/header]
   (case @global/path
     "/" [:p "Home is where the heart lies..."]

     :else [:div [:p "Not found"]])])

(defn -main []
  (router/init-router)
  (r/render-component [body]
    (dom/getElement "app")))

(set! (.-onload js/window) -main)

(defn rerender []
  (let [node (dom/getElement "app")]
    (r/unmount-component-at-node node)
    (r/render-component [body]
      node)))

(defn ^:export reload []
  (rerender))
