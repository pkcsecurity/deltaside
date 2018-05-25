(ns deltaside.model.game
  (:require [clojure.spec.alpha :as s]
            [deltaside.spec :as spec]))

(s/def ::game-id spec/uuid)
(s/def ::game-name spec/string)

(def example-game-board {"54947df8-0e9e-4471-a2f9-9af509fb5889"
                         {:name "Spencer's Sick Deltaside game"
                          :admin 1                          ;-> player id
                          :board (atom
                                   {:players
                                    [{:text "Someone 1"
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
                                     :y-vel 50}]
                                    :objects [{:text "~"
                                               :color "blue"
                                               :id "asdfasdfasfdasdf some UUID"
                                               :type :projectile
                                               :angle 0
                                               :x 300
                                               :y 400
                                               :x-vel 50
                                               :y-vel 50}]})}})

(def games (atom example-game-board)) ; TODO change back

(def player-defaults
   {:positon [0 0]
    :velocity [0 0]
    :orientation 0})

(def projectile-defaults
  {:positon [0 0]
   :velocity [0 0]
   :orientation 0})

(def astroid-defaults
  {:positon [0 0]
   :velocity [0 0]
   :orientation 0})
