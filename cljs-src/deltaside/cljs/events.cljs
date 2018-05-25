(ns deltaside.cljs.events
  (:require [deltaside.cljs.model.player :as p]
            [deltaside.cljs.model.global :as global]
            [deltaside.cljs.xhr :as xhr]))

(defn wrap-coord [coord]
  (mod coord 500))

(defn iterate-entity [seconds {:keys [x-vel y-vel] :as entity}]
  (-> entity
      (update :x + (* seconds x-vel))
      (update :y + (* seconds y-vel))
      (update :x wrap-coord)
      (update :y wrap-coord)))

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

(defn iterate-velocities [seconds entities]
  (map (comp (partial iterate-player seconds)
             (partial iterate-entity seconds)) entities))

(defn get-player []
  (first (filter current-player? @p/entities)))

(defn new-projectile []
  (let [now (.getTime (js/Date.))
        time-since (- now @p/last-projectile-time)]
    (when (< 1000 time-since)
      (reset! p/last-projectile-time now)
      (let [{:keys [x-vel y-vel] :as player} (get-player)]
        (-> player
            (assoc :id (str "projectile-" (Math/random)))
            (update :x + (* 0.2 x-vel))
            (update :y + (* 0.2 y-vel))
            (update :x-vel * 1.5)
            (update :y-vel * 1.5)
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

  (.setInterval js/window update-game-board 2000)

  (.addEventListener js/document "mousemove"
                     (fn [e]
                       (reset! p/mouse-x (.-pageX e))
                       (reset! p/mouse-y (.-pageY e)))))