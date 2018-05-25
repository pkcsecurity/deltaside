(ns deltaside.core
  (:gen-class)
  (:require [ring.middleware.json :as json]
            [ring.middleware.file :as file]
            [ring.middleware.content-type :as ct]
            [deltaside.auth :as roles]
            [deltaside.routes :as routes]
            [deltaside.properties :as props]
            [immutant.web :as server]))

(def app
  (-> routes/routes
    (json/wrap-json-response)
    (json/wrap-json-body {:keywords? true})
    (roles/wrap-security)
    (file/wrap-file "static" {:index-files? false})
    (ct/wrap-content-type)))

(defn -main [& args]
  (if props/prod?
    (server/run app
      :host "0.0.0.0"
      :port (props/environment "PORT"))
    (server/run-dmc app
      :host "127.0.0.1"
      :port "8080")))
