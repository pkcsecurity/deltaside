(ns deltaside.core
  (:gen-class)
  (:require [ring.middleware.json :as json]
            [ring.middleware.file :as file]
            [ring.middleware.content-type :as ct]
            [deltaside.auth :as auth]
            [deltaside.routes :as routes]
            [deltaside.properties :as props]
            [immutant.web :as server]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kw-params]
            [deltaside.spec :as spec]))

(def app
  (-> routes/routes
    (json/wrap-json-response)
    (json/wrap-json-body {:keywords? true})
    (auth/wrap-security)
    (file/wrap-file "static" {:index-files? false})
    (kw-params/wrap-keyword-params)
    (params/wrap-params)
    (spec/wrap-conform-failure)))


(defn -main [& args]
  (if props/prod?
    (server/run app
      :host "0.0.0.0"
      :port (props/environment "PORT"))
    (server/run-dmc app
      :host "127.0.0.1"
      :port "8080")))
