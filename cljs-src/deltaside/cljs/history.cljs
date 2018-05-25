(ns deltaside.cljs.history
  (:require
    [goog.events :as ev]
    [goog.history.Html5History]
    [goog.history.Html5History.TokenTransformer]
    [goog.history.EventType :as EventType]
    [deltaside.cljs.model.global :as global]
    [secretary.core :as sec :refer-macros [defroute]]))

(defn get-url
  ([] (get-url js/location))
  ([location]
   (str (.-pathname location) (.-search location))))

(defn on-url-change [e]
  (let [url (get-url)
        path (.-pathname js/location)]
    (reset! global/path path)
    (when-not (.-isNavigation e)
      (.scrollTo js/window 0 0))
    (sec/dispatch! url)))

(def token-transformer
  (let [ttf (goog.history.Html5History.TokenTransformer.)]
    (set! (.-createUrl ttf)
      (fn [token path-prefix location]
        (str path-prefix token)))

    (set! (.-retrieveToken ttf)
      (fn [path-prefix location]
        (get-url location)))
    ttf))

(def history
  (let [h (goog.history.Html5History. nil token-transformer)]
    (ev/listen h EventType/NAVIGATE on-url-change)
    (doto h
      (.setPathPrefix (str (.-protocol js/location)
                        "//"
                        (.-host js/location)))
      (.setUseFragment false)
      (.setEnabled true))
    h))
