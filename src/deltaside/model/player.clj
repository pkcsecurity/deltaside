(ns deltaside.model.player
  (:require [clojure.spec.alpha :as s]
            [deltaside.spec :as spec]))


(s/def ::player-id spec/numeric)
(s/def ::player-name spec/string)