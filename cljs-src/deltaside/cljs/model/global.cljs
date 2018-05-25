(ns deltaside.cljs.model.global
  (:require [reagent.core :as r]))

(def path (r/atom "/"))

(def global-stats (r/atom {}))
