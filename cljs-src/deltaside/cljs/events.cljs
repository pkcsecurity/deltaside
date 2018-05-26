(ns deltaside.cljs.events
  (:require [deltaside.cljs.model.player :as p]
            [deltaside.cljs.model.global :as global]
            [deltaside.cljs.xhr :as xhr]))

(defn wrap-coord [coord]
  (mod coord 500))

(defn iterate-entity [seconds {:keys [x-vel y-vel] :as entity}]
  (-> entity
      (update :x + (* seconds x-vel))
      (update :y + (* seconds y-vel))))

(defn angle-against-mouse [{:keys [x y]}]
  (let [dx (- @p/mouse-x x)
        dy (- @p/mouse-y y)]
    (+ (when (> 0 dx) Math/PI) (Math/atan (/ dy dx)))))

(defn current-player? [{:keys [type id]}]
  (and (= type "player")
       (= id @p/player-id)))

(defn iterate-player [seconds {:keys [type] :as entity}]
  (if (current-player? entity)
    (-> entity
        (update :x-vel + (* seconds @p/x-thrust))
        (update :y-vel + (* seconds @p/y-thrust))
        (assoc :angle (angle-against-mouse entity)))
    entity))

(defn should-destroy? [{:keys [x y type]}]
  (when (= type :projectile)
    (println x y)
    (not (and (< 0 x 500)
              (< 0 y 500)))))

(defn iterate-velocities [seconds entities]
  (->> entities
       (map (comp (partial iterate-player seconds)
                  (partial iterate-entity seconds)))
       (remove should-destroy?)
       (map #(update % :x wrap-coord))
       (map #(update % :y wrap-coord))))

(defn get-player []
  (first (filter current-player? @p/entities)))

(defn new-projectile []
  (let [now (.getTime (js/Date.))
        time-since (- now @p/last-projectile-time)]
    (when (< 200 time-since)
      (reset! p/last-projectile-time now)
      (let [{:keys [angle] :as player} (get-player)
            x-vel (* 200 (Math/cos angle))
            y-vel (* 200 (Math/sin angle))]
        (-> player
            (assoc :id (str "projectile-" (Math/random)))
            (update :x + (* 0.2 x-vel))
            (update :y + (* 0.2 y-vel))
            (assoc :x-vel x-vel)
            (assoc :y-vel y-vel)
            (assoc :text "~")
            (assoc :type :projectile))))))

(defn update-game-board []
  (xhr/ajax "GET" "/api/v1/game/54947df8-0e9e-4471-a2f9-9af509fb5889"
            :on-success
            (fn [{:keys [board]}]
              (let [{:keys [players objects]} board]
                (reset! p/entities (concat players objects))))))

(defn setup-events []
  (.addEventListener js/document "keydown"
                     (fn [e]
                       (case (.-key e)
                         "d" (reset! p/x-thrust p/power)
                         "a" (reset! p/x-thrust p/rpower)
                         "w" (reset! p/y-thrust p/rpower)
                         "s" (reset! p/y-thrust p/power)
                         "ArrowRight" (reset! p/x-thrust p/power)
                         "ArrowLeft" (reset! p/x-thrust p/rpower)
                         "ArrowUp" (reset! p/y-thrust p/rpower)
                         "ArrowDown" (reset! p/y-thrust p/power)
                         nil)))
  (.addEventListener js/document "keyup"
                     (fn [e]
                       (case (.-key e)
                         "d" (reset! p/x-thrust 0)
                         "a" (reset! p/x-thrust 0)
                         "w" (reset! p/y-thrust 0)
                         "s" (reset! p/y-thrust 0)
                         "ArrowRight" (reset! p/x-thrust 0)
                         "ArrowLeft" (reset! p/x-thrust 0)
                         "ArrowUp" (reset! p/y-thrust 0)
                         "ArrowDown" (reset! p/y-thrust 0)
                         nil)))
  (.addEventListener js/document "mousedown"
                     (fn []
                       (if-let [projectile (new-projectile)]
                         (swap! p/entities #(conj % projectile)))))
  (.setInterval js/window
                ; TODO don't use setinterval
                (fn []
                  (when (= @global/screen :game) (swap! p/entities (partial iterate-velocities 0.02))))
                20)

  (.setInterval js/window update-game-board 200000)
  (update-game-board)

  (.addEventListener js/document "mousemove"
                     (fn [e]
                       (reset! p/mouse-x (.-pageX e))
                       (reset! p/mouse-y (.-pageY e)))))