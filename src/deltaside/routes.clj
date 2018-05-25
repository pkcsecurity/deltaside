(ns deltaside.routes
  (:require [compojure.core :as r]
            [compojure.route :as route]
            [deltaside.index :as index]
            [deltaside.route.player :as player]
            [deltaside.route.game :as game]))

(defn index-route [_]
  {:status 200
   :body index/index
   :headers {"Content-Type" "text/html"}})

(defn index-404 [_]
  {:status 404
   :body index/index-404
   :headers {"Content-Type" "text/html"}})

(r/defroutes routes
  (r/GET "/" [] index-route)
  (r/context "/api/v1" []
    (r/POST "/player" [] player/create-player) ;Return game board
    (r/GET "/player" [] player/get-player) ;Return player
    (r/PUT "/player" [] player/update-player) ;Return updated game board
    (r/DELETE "/player" [] player/delete-player)

    (r/POST "/game" [] game/create-game) ;Return new game board with redirect
    (r/GET "/game" [] game/get-all-games) ;Return list of all game boards
    (r/GET "/game/:id" [id] game/get-game) ;Return game board
    (r/DELETE "/game/:id" [id] game/delete-game))
  (route/not-found index-404))
