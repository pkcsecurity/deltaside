(ns deltaside.route.player
  (:require [clojure.tools.logging :as log]
            [clojure.spec.alpha :as spec]
            [deltaside.http :as http]
            [deltaside.controller.player :as ctrl]
            [deltaside.model.player :as model]
            [deltaside.model.game :as game-model]
            [deltaside.controller.game :as game-ctrl]))

(defn get-player [req]
  (log/info "Retrieving player...")
  (http/ok))

;TODO Get game-id from url
(defn create-player [{{:keys [text]} :body}]
  (log/info "Creating player '"text"'...")
  (if (spec/valid? ::model/player-name text)
    (if-let [player (ctrl/add-player text (get @game-model/games game-model/default-game-id))]
      (http/ok (game-ctrl/get-game game-model/default-game-id))
      (http/conflict {:error "Unable to add player to game board."}))
    (http/bad-request {:error "Name is invalid."})))

(defn update-player [req]
  (log/info "Updating player...")
  (http/ok))

(defn delete-player [req]
  (log/info "Deleting player...")
  (http/ok))

