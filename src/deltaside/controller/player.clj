(ns deltaside.controller.player)

(def MAX_X 500)
(def MAX_Y 500)
(def MAX_PLAYERS 20)

(defn create-player [text id & {:keys [color angle x y x-vel y-vel]
                             :or {color "red"
                                  angle 0
                                  x (rand-int MAX_X)
                                  y (rand-int MAX_Y)
                                  x-vel 0
                                  y-vel 0}}]
  {:text text
   :id id
   :color color
   :type :player
   :angle angle
   :x x
   :y y
   :x-vel x-vel
   :y-vel y-vel})

;TODO: This needs to be synchronous
;TODO: Players is a list not a map. Needs to do a filter
(defn new-player-id [game-map]
  (let [id (rand-int MAX_PLAYERS)]
    (println "random id is..." id)
    (cond
      (<= MAX_PLAYERS (count (:players @(:board game-map)))) nil
      (get (:players @(:board game-map)) id) (new-player-id game-map)
      :else id)))

;TODO make sure player-id doesn't already exist
;TODO make sure game-id doesn't already exist
(defn add-player [player-text game-map]
  "Creates a new player with defaults in the game"
  (if-let [id (new-player-id game-map)]
    (let [player (create-player player-text id)]
      (swap! (:board game-map) assoc :players (conj (:players @(:board game-map)) player))
      player)))
