(ns deltaside.routes
  (:require [compojure.core :as r]
            [compojure.route :as route]
            [deltaside.index :as index]))

(defn index-route [_]
  {:status 200
   :body index/index
   :headers {"Content-Type" "text/html"}})

(r/defroutes routes
  (r/GET "/" [] index-route)
  (route/not-found nil))
