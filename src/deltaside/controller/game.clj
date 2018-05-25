(ns deltaside.controller.game
  (:require [deltaside.model.game :as model]
            [deltaside.utils :as utils]))

;TODO make sure player-id doesn't already exist
;TODO make sure game-id doesn't already exist
(defn add-player [game-id player-id]
  "Creates a new player with defaults on the game board"
  (let [board (get @model/games game-id)
        players (:players (:board board))
        player (model/player-defaults)
        new-players (assoc players player-id player)]
    (swap! (:board board) assoc :players new-players)))

;TODO make sure player-id doesn't already exist
;TODO make sure game-id doesn't already exist
;TODO overrirde defaults with passed in params
(defn add-object [game-id object-id object-type & {:keys [x y dx dy angle]
                                                   :or {x 0 y 0 dx 0 dy 0 angle 0}}]
  "Creates a new object with defaults (or customs)"
  (let [board (get @model/games game-id)
        objects (:objects (:board board))
        object (if (= :astroid object-type)
                 (model/astroid-defaults)
                 (model/projectile-defaults))
        new-objects (assoc objects object-id object)]
    (swap! (:board board) assoc :objects new-objects)))

(defn get-game [id]
  "Returns the current state of the game (removes atoms)"
  (let [board (get @model/games id)]
    (merge board {:board @(:board board)})))

(defn get-all-games [id]
  "Returns the current state of all games (removes atoms)"
  (let [games {}]
    (doseq [[k v] @model/games]
      (merge games {k (merge v (:board @(:board v)))}))
    games))

(defn new-game [name id]
  (let [game-id (utils/uuid)]
    (swap! model/games assoc game-id {:name name
                                      :id id})
    (add-player game-id id)
    game-id))

(defn delete-game [id]
  (swap! model/games dissoc id))