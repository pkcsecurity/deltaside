(ns deltaside.cljs.events
  (:require [deltaside.cljs.model.player :as p]
            [deltaside.cljs.model.global :as global]))

(defn iterate-vel [seconds {:keys [x-vel y-vel] :as entity}]
  (-> entity
      (update :x + (* seconds x-vel))
      (update :y + (* seconds y-vel))))

(defn angle-against-mouse [{:keys [x y]}]
  (let [dx (- @p/mouse-x x)
        dy (- @p/mouse-y y)]
    (+ (when (> 0 dx) Math/PI) (Math/atan (/ dy dx)))))

(defn iterate-player [seconds {:keys [player?] :as entity}]
  (if player?
    (-> entity
        (update :x-vel + (* seconds @p/x-thrust))
        (update :y-vel + (* seconds @p/y-thrust))
        (assoc :angle (angle-against-mouse entity)))
    entity))

(defn iterate-velocities [seconds entities]
  (map (comp (partial iterate-player seconds) (partial iterate-vel seconds)) entities))

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
  (.setInterval js/window
                ; TODO don't use setinterval
                (fn []
                  (when (= @global/screen :game) (swap! p/entities (partial iterate-velocities 0.02))))
                20)
  (.addEventListener js/document "mousemove"
                     (fn [e]
                       (reset! p/mouse-x (.-pageX e))
                       (reset! p/mouse-y (.-pageY e)))))