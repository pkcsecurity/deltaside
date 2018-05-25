(ns deltaside.route.game
  (:require [clojure.tools.logging :as log]
            [deltaside.http :as http]
            [deltaside.model.game :as model]
            [deltaside.controller.game :as ctrl]
            [clojure.spec.alpha :as spec]))

(defn get-game [{{:keys [id]} :route-params}]
  (log/info (str "Retrieving game " id " ..."))
  (if (and
        (spec/valid? ::model/game-id id)
        (get @model/games id))
    (http/ok (ctrl/get-game id))
    (http/bad-request {:error "Invalid game id."})))

(defn get-all-games [_]
  (log/info "Retrieving all games...")
  (http/ok (ctrl/get-all-games)))

(defn create-game [{{:keys [name]} :body {:keys [id]} :sub}]
  (log/info "Creating new game...")
  (if-let [name (spec/conform ::model/game-name name)]
    (let [game-id (ctrl/new-game name id)]
      (http/redirect (str "/" game-id)))
    (http/bad-request {:error "Name must be a string between 0-256 characters"})))

(defn delete-game [{:keys [route-params] {:keys [id]} :sub}]
  (log/info (str "Deleting game " id " ..."))
  (if-let [game-id (spec/conform ::model/game-id (:id route-params))]
    (if ((:admin (ctrl/get-game game-id)) id)
      (ctrl/delete-game id)
      http/forbidden)))
