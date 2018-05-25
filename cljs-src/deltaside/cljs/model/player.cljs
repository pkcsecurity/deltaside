(ns deltaside.cljs.model.player
  (:require [reagent.core :as r]))

(def entities (r/atom [{:text "Someone 1"
                        :id 1
                        :color "red"
                        :type :player
                        :angle 0
                        :x 500
                        :y 200
                        :x-vel 50
                        :y-vel -50}
                       {:text "Someone 2"
                        :color "green"
                        :id 2
                        :type :player
                        :angle 0
                        :x 200
                        :y 200
                        :x-vel 50
                        :y-vel -50}
                       {:text "Someone 3"
                        :color "yellow"
                        :id 3
                        :type :player
                        :angle 0
                        :x 300
                        :y 300
                        :x-vel -50
                        :y-vel -50}
                       {:text "Someone 4"
                        :color "blue"
                        :id 4
                        :type :player
                        :angle 0
                        :x 300
                        :y 400
                        :x-vel 50
                        :y-vel 50}]))

(def x-thrust (r/atom 0))

(def y-thrust (r/atom 0))

(def power 500)

(def rpower -500)

(def mouse-x (r/atom 0))

(def mouse-y (r/atom 0))

(def last-projectile-time (r/atom 0))

(def player-id (r/atom 1))