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
  [{:uris ["/"]
    :handler allow-all}
   {:pattern #"^/static/.*$"
    :handler allow-all}
   {:pattern #"^/.*$"
    :handler deny-all}])

(defn wrap-security [app]
  (-> app
    (authz/wrap-access-rules {:rules rules})
    (mw/wrap-authentication auth-backend)
    (mw/wrap-authorization auth-backend)))

