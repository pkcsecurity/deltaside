(ns deltaside.cljs.model.global
  (:require [reagent.core :as r]))

(def path (r/atom "/"))

(def screen (r/atom :menu))

(def global-stats (r/atom {}))

(def rooms (r/atom [{:id 1 :name "Awesome room"}
                    {:id 2 :name "Mediocre room"}]))