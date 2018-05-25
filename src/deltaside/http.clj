(ns deltaside.http
  (:require [ring.util.response :as ring-response]))

(defn ok
  ([] {:status 204})
  ([x]
   {:status 200
    :body x}))

(defn bad-request
  ([] {:status 400})
  ([x]
   {:status 400
    :body x}))

(def unauthorized
  {:status 401
   :headers {"WWW-Authenticate" "Bearer realm=\"sbgive.org\""}})

(def forbidden
  {:status 403
   :body "Forbidden"})

(defn redirect [url]
  (ring-response/redirect url))

(def payment {:status 402})
(def conflict {:status 409})
(def internal-server-error {:status 500})

(def not-implemented {:status 501})
(def service-unavailable
  {:status 501
   :headers {"Retry-After" (* 60 10)}})