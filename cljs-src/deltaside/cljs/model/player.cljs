(ns deltaside.cljs.model.player
  (:require [reagent.core :as r]))

(def entities (r/atom [{:text "Someone 1"
                        :player? true
                        :angle 0
                        :x 500
                        :y 200
                        :x-vel 50
                        :y-vel -50}
                       {:text "Someone 2"
                        :angle 0
                        :x 200
                        :y 200
                        :x-vel 50
                        :y-vel -50}
                       {:text "Someone 3"
                        :angle 0
                        :x 300
                        :y 300
                        :x-vel -50
                        :y-vel -50}
                       {:text "Someone 4"
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