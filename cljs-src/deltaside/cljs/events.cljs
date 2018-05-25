(ns deltaside.cljs.events
  (:require [deltaside.cljs.model.player :as p]))

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
                         "s" (reset! p/y-thrust 0))))
  (.setInterval js/window
                ; TODO don't use setinterval
                (fn []
                  (swap! p/x-vel + @p/x-thrust)
                  (swap! p/y-vel + @p/y-thrust)
                  (swap! p/x + @p/x-vel)
                  (swap! p/y + @p/y-vel)
                  (let [dx (- @p/mouse-x @p/x)
                        dy (- @p/mouse-y @p/y)]
                    (reset! p/angle (Math/atan (/ dy dx)))))

                20)
  (.addEventListener js/document "mousemove"
                     (fn [e]
                       (reset! p/mouse-x (.-pageX e))
                       (reset! p/mouse-y (.-pageY e)))))