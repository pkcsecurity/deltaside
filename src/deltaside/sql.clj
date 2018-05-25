(ns deltaside.sql
  (:require [clojure.java.jdbc :as jdbc]))

(def heroku-db-url (System/getenv "JDBC_DATABASE_URL"))

(def dbspec
  (if heroku-db-url
    {:connection-uri heroku-db-url}
    {:subprotocol "postgresql"
     :subname "//localhost:5432/deltaside"}))

(defmacro insert! [table row]
  `(jdbc/insert! dbspec ~table ~row))

(defmacro query [q]
  `(jdbc/query dbspec ~q))

(defmacro update! [table row where]
  `(jdbc/update! dbspec
     ~table
     ~row
     ~where))

(defmacro delete! [table where]
  `(jdbc/delete! dbspec ~table ~where))
