(ns deltaside.auth
  (:require [buddy.auth :as auth]
            [buddy.auth.accessrules :as authz]
            [buddy.auth.backends.token :as token]
            [buddy.auth.middleware :as mw]
            [deltaside.properties :as props]))

(defonce tokens (atom {}))
(def allow-all (constantly true))
(def deny-all (constantly false))

(defn store-token [token obj]
  (swap! tokens assoc token obj))

(defn token-auth-fn [req token]
  (when-let [{:keys [id email]} (get @tokens token)]
    {:sub id
     :email email}))

(def auth-backend
  (token/token-backend {:token-name "Bearer"
                        :authfn token-auth-fn}))

(def rules
  [
   ;Defaults
   {:uris ["/"]
    :handler allow-all}
   {:pattern #"^/static/.*$"
    :handler allow-all}

   ;Player routes
   {:uri "/api/v1/player"
    :handler allow-all
    :request-method :get}
   {:uri "/api/v1/player"
    :handler token-auth-fn
    :request-method #{:post :put :delete}}

   ;Game routes
   {:uri "/api/v1/game"
    :handler allow-all
    :request-method #{:get :post}}
   {:patterns #"/api/v1/game/[\da-fA-F\-]{40}$"
    :handler allow-all
    :request-method :get}
   {:patterns #"/api/v1/game/[\da-fA-F]{40}$"
    :handler token-auth-fn
    :request-method :delete}

   ;Else
   {:pattern #"^/.*$"
    :handler deny-all}])

(defn wrap-security [app]
  (-> app
    (authz/wrap-access-rules {:rules rules})
    (mw/wrap-authentication auth-backend)
    (mw/wrap-authorization auth-backend)))

