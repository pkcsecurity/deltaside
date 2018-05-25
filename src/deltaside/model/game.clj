(ns deltaside.model.game
  (:require [clojure.spec.alpha :as s]
            [deltaside.spec :as spec]))

(s/def ::game-id spec/uuid)
(s/def ::game-name spec/string)

(def example-game-board {"54947df8-0e9e-4471-a2f9-9af509fb5889"
                         {:name "Spencer's Sick Deltaside game"
                          :admin 1                          ;-> player id
                          :board (atom
                                   {:players {1             ;-> player id
                                              {:positon [0 0] ;-> [x, y]
                                               :velocity [1 -1] ;-> [dx, dy]
                                               :orientation 36} ;-> degrees
                                              2             ; -> player id
                                              {:positon [0 0] ;-> [x, y]
                                               :velocity [1 -1] ;-> [dx, dy]
                                               :orientation 36}} ;-> degrees
                                    :objects {1             ;-> object id
                                              {:type :projectile
                                               :owner 1     ;-> player id
                                               :positon [0 0] ;-> [x, y]
                                               :velocity [1 -1] ;-> [dx, dy]
                                               :orientation 36} ;-> degrees
                                              2             ;-> object id
                                              {:type :astroid
                                               :owner -1
                                               :positon [0 0] ;-> [x, y]
                                               :velocity [1, -1] ;-> [dx, dy]
                                               :orientation 36}}})}}) ;-> degrees}}

(def games (atom {}))

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
