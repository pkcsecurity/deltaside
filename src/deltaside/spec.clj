(ns deltaside.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.tools.logging :as log]
            [deltaside.utils :as utils]
            [deltaside.http :as http]
            [deltaside.properties :as props]))

(defn explain [spec obj]
  (when-let [probs (s/explain-data spec obj)]
    (with-out-str
      (s/explain-out probs))))

(defmacro string-max-n [n]
  `(s/and string? utils/not-blank? #(< (count %) ~n)))

(defmacro string-of-length [n]
  `(s/and string? utils/not-blank? #(= (count %) ~n)))

(def string (string-max-n 256))
(def long-string (string-max-n 1024))

(def email (s/and string utils/valid-email?))

(def strong-password (s/and string #(>= (count %) 8)))

(def numeric
  (s/and
    string?
    utils/not-blank?
    (partial re-matches #"[0-9]+")))

(def uuid
  (s/and
    (string-of-length 36)
    #(try
       (utils/uuid %)
       (catch Exception _))))

; NOTE:
; When we are in prod, we don't want to leak info about our
; validators.
(defn wrap-conform-failure [handler]
  (fn [req]
    (try
      (handler req)
      (catch clojure.lang.ExceptionInfo ei
        (log/warn (.getMessage ei))
        (if-let [kw (::error (ex-data ei))]
          (http/bad-request (if props/prod? (str "error: " kw) (.getMessage ei)))
          (if props/prod?
            http/internal-server-error
            (throw ei)))))))



