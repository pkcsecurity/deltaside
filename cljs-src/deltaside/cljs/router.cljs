(ns deltaside.cljs.router
  (:require [secretary.core :as sec :refer-macros [defroute]]
            [deltaside.cljs.history :as h]
            [clojure.string :as str]))

(defn set-title [t]
  (set! (.-title js/document) (str t " | PeopleGroups.org")))

(defn query-string []
  (.substring (.-search js/location) 1))

(defn query-value [k]
  (.get (goog.Uri.QueryData. (query-string))
    (name k)))

(defn path []
  (.-pathname js/location))

(defn create-query-string [& kvs]
  (let [qd (goog.Uri.QueryData. "")]
    (loop [[k v & kvs] kvs]
      (when k
        (if v
          (.set qd (name k) v)
          (.remove qd (name k)))
        (recur kvs)))
    (str qd)))

(defn with-query [url & kvs]
  (let [qs (apply create-query-string kvs)]
    (str url (when (not (str/blank? qs)) (str "?" qs)))))

(defn navigate [url & {:keys [query-params]}]
  (let [url (apply with-query url (flatten (seq query-params)))]
    (.setToken h/history url)))

(defn init-router []
  (defroute "*" []
    (set-title "Home"))
  (sec/dispatch! (h/get-url)))
