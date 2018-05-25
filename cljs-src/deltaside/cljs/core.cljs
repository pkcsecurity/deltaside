(ns deltaside.cljs.core
  (:require [reagent.core :as r]
            [goog.dom :as dom]
            [deltaside.cljs.xhr :as xhr]
            [deltaside.cljs.events :as events]
            [deltaside.cljs.model.global :as global]
            [deltaside.cljs.model.player :as p]
            [deltaside.cljs.router :as router]))


(defn in-game []
  [:div
   (doall (for [{:keys [id x y type angle color text]} @p/entities]
     ^{:key id}
     [:div {:style {:position  "fixed"
                    :font-weight (if (= type :me) :bold :initial)
                    :color color
                    :transform (str "translateX(-50%) translateY(-50%) rotate(" angle "rad)")
                    :top       y
                    :left      x}}
      [:p text]]))])

(defn join-game [id]
  (reset! global/screen :game))

(defn main-menu []
  [:div.center.p3
   [:div.h1.p3 "DELTASIDE"]
   [:div.h3 "Available rooms:"]
   (for [{:keys [id name]} @global/rooms]
     ^{:key id}
     [:div
      [:a
       {:on-click (partial join-game id)}
       name]])])

(enable-console-print!)

(defn body []
  [:div
   (case @global/screen
     :menu [main-menu]
     :game [in-game]

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