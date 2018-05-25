(ns deltaside.cljs.core
  (:require [reagent.core :as r]
            [goog.dom :as dom]
            [deltaside.cljs.xhr :as xhr]
            [deltaside.cljs.events :as events]
            [deltaside.cljs.model.global :as global]
            [deltaside.cljs.model.player :as p]
            [deltaside.cljs.router :as router]))


(defn content []
  [:div
   (doall (for [{:keys [x y angle text]} @p/entities]
     ^{:key text}
     [:div {:style {:position  "fixed"
                    :transform (str "translateX(-50%) translateY(-50%) rotate(" angle "rad)")
                    :top       y
                    :left      x}}
      [:p text]]))])

(enable-console-print!)

(defn body []
  [:div
   ;[layout/header]
   (case @global/path
     "/" [content]

     :else [:div [:p "Not found"]])])

; TODO hook into react lifecycle properly for this
(events/setup-events)

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