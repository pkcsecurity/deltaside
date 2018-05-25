(ns deltaside.cljs.model.player
  (:require [reagent.core :as r]))

(def angle (r/atom 0))

(def x (r/atom 0))

(def y (r/atom 0))

(def x-thrust (r/atom 0))

(def y-thrust (r/atom 0))

(def x-vel (r/atom 0))

(def y-vel (r/atom 0))

(def power 0.1)

(def rpower -0.1)

(def mouse-x (r/atom 0))

(def mouse-y (r/atom 0))