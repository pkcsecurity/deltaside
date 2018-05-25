(ns deltaside.cljs.xhr
  (:import [goog.net XhrIoPool EventType]
           [goog.net.XhrIo ResponseType])
  (:require goog.net.EventType
            [reagent.core :as r]
            [deltaside.cljs.utils :as utils]
            [deltaside.cljs.storage :as storage]))

(declare ajax)

(def min-pool-size 1)
(def max-pool-size 10)
(def pool (XhrIoPool. (js/Map.) min-pool-size max-pool-size))

(def buffer-type (.-ARRAY_BUFFER ResponseType))
(def blob-type (.-BLOB ResponseType))
(def default-type (.-DEFAULT ResponseType))
(def document-type (.-DOCUMENT ResponseType))
(def json-type (.-JSON ResponseType))
(def string-type (.-TEXT ResponseType))


(def token (r/atom (storage/storage-get ::token)))

(defn set-token [t]
  (reset! token t)
  (storage/storage-set ::token t))

(defn get-token []
  (if @token
    @token
    (storage/storage-get ::token)))

(defn logout []
  (reset! token nil)
  (storage/storage-clear))

(defn response-types [t]
  (case t
    :buffer buffer-type
    :blob blob-type
    :default default-type
    :document document-type
    :json json-type
    :string string-type))

(defn on-io-result [response on-success on-error on-finally & {:keys [include-response-headers?]}]
  (fn [e]
    (let [io (.-target e)
          success? (.isSuccess io)
          status (.getStatus io)
          unauthorized? (.getResponseHeader io "www-authenticate")
          json? (= json-type (.getResponseType io))
          headers (clojure.walk/keywordize-keys
                    (into (hash-map)
                      (mapv #(clojure.string/split % #":\s" 2)
                        (clojure.string/split-lines (.getAllResponseHeaders io)))))]
      (try
        (cond
          (and (or (= status 200) (= status 201)) json?)
          (on-success (if include-response-headers?
                        {:body (when-not (empty? (.getResponse io)) (js->clj (.getResponseJson io) :keywordize-keys true))
                         :headers headers}
                        (when-not (empty? (.getResponse io)) (js->clj (.getResponseJson io) :keywordize-keys true))))
          (= status 200) (on-success (.getResponse io))
          success? (on-success nil)
          :else (on-error (.getResponse io) io))
        (finally
          (try
            (on-finally io)
            (finally
              (.releaseObject pool io))))))))

(defn default-error [_ io]
  (.error js/console
    (.getLastUri io)
    (.getLastError io)
    (.getResponse io)))

(defn ajax [method url & {:keys [body
                                 timeout-millis
                                 headers
                                 response-type
                                 include-response-headers?
                                 authorize?
                                 on-success
                                 on-error
                                 on-finally]
                          :or {timeout-millis 0
                               headers {}
                               response-type :json
                               include-response-headers? false
                               authorize? true
                               on-success utils/nop
                               on-error default-error
                               on-finally utils/nop}
                          :as opts}]
  (.getObject pool
    (fn [io]
      (let [body (cond
                   (not body) nil
                   (get headers "Content-Type") body
                   :else (.stringify js/JSON (clj->js body)))
            headers (merge {"Content-Type" "application/json"}
                      (when (and authorize? (get-token))
                        {"Authorization" (str "Bearer " (get-token))})
                      headers)]
        (doto io
          (.setTimeoutInterval timeout-millis)
          (.setResponseType (response-types response-type))
          (.send url
            method
            body
            headers)
          (.listenOnce (.-COMPLETE EventType)
            (on-io-result response-type on-success on-error on-finally :include-response-headers? include-response-headers?)))))))
