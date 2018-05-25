(ns deltaside.route.player
  (:require [clojure.tools.logging :as log]
            [deltaside.http :as http]
            [deltaside.model.game :as game]))

(defn get-player [req]
  (log/info "Retrieving player...")
  (http/ok))

(defn create-player [req]
  (log/info "Creating player...")
  (http/ok))

(defn update-player [req]
  (log/info "Updating player...")
  (http/ok))

(defn delete-player [req]
  (log/info "Deleting player...")
  (http/ok))

