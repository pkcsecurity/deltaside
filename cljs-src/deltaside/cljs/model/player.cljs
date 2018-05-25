(ns deltaside.cljs.model.player
  (:require [reagent.core :as r]))

(def entities (r/atom [{:text "Someone 1"
                        :player? true
                        :angle 0
                        :x 500
                        :y 200
                        :x-vel 1
                        :y-vel -1}
                       {:text "Someone 2"
                        :angle 0
                        :x 200
                        :y 200
                        :x-vel 1
                        :y-vel -1}
                       {:text "Someone 3"
                        :angle 0
                        :x 300
                        :y 300
                        :x-vel -1
                        :y-vel -1}
                       {:text "Someone 4"
                        :angle 0
                        :x 300
                        :y 400
                        :x-vel 1
                        :y-vel 1}]))

(def x-thrust (r/atom 0))

(def y-thrust (r/atom 0))

(def power 0.1)

(def rpower -0.1)

(def mouse-x (r/atom 0))

(def mouse-y (r/atom 0))