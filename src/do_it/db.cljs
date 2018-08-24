(ns do-it.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::email string?)
(s/def ::password string?)
(s/def ::error string?)
(s/def ::user-uid string?)
(s/def ::loading boolean?)
(s/def ::auth
  (s/keys :req-un [::email ::password ::error ::user-uid ::loading]))

(s/def ::app-db
  (s/keys :req-un [::auth]))

;; initial state of app-db
(def app-db {:auth {:email ""
                    :password ""
                    :error ""
                    :user-uid ""
                    :loading false}})
