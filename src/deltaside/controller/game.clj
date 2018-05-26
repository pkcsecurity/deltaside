(ns deltaside.controller.game
  (:require [deltaside.model.game :as model]
            [deltaside.utils :as utils]
            [deltaside.controller.player :as player-ctrl]))

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

(defn get-all-games []
  "Returns the current state of all games (removes atoms)"
  (let [games {}]
    (doseq [[k v] @model/games]
      (merge games {k (merge v (:board @(:board v)))}))
    games))

(defn new-game [name id]
  (let [game-id (utils/uuid)]
    (swap! model/games assoc game-id {:name name
                                      :id id})
    ;(player-ctrl/add-player "default" game-id id)
    game-id))

(defn delete-game [id]
  (swap! model/games dissoc id))