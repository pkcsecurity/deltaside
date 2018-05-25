(ns deltaside.cljs.storage
  (:require [goog.net.cookies])
  (:import [goog.storage.mechanism HTML5LocalStorage HTML5SessionStorage]))

(defn get-cookie [k] (.get goog.net.cookies k))

(defn set-cookie [k v & {:keys [age path]
                         :or {age -1}}]
  (.set goog.net.cookies k v age path))

(defn remove-cookie [k & {:keys [path]}]
  (.remove goog.net.cookies k path))

(def local-storage (HTML5LocalStorage.))
(def session-storage (HTML5SessionStorage.))

(defn session-set [k v]
  (.set session-storage k v))

(defn session-get [k]
  (.get session-storage k))

(defn session-clear []
  (.clear session-storage))

(defn storage-set [k v]
  (.set local-storage (str k) v))

(defn storage-get [k]
  (.get local-storage (str k)))

(defn storage-clear []
  (.clear local-storage))
