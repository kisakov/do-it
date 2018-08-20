(ns do-it.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx after]]
   [clojure.spec.alpha :as s]
   [do-it.db :as db :refer [app-db]]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
 :initialize-db
 validate-spec
 (fn [_ _]
   app-db))

(defn- login-screen [db]
  (assoc db :routing (clj->js {:index  0
                               :routes [{:index 0
                                         :routes [{:key "login"
                                                   :routeName "login"}]
                                         :key       "LoginStack"
                                         :routeName "LoginStack"}]})))

(reg-event-fx
 :reset-routing-state
 validate-spec
 (fn [cofx _]
   {:db (login-screen (:db cofx))
    :firebase/auth-user {}}))

(reg-event-db
 :login-field-update
 validate-spec
 (fn [db [_ prop value]]
   (assoc-in db [:auth prop] value)))

(reg-event-fx
 :login-user
 (fn [cofx _]
   (let [db (:db cofx)
         {:keys [email password]} (:auth db)]
     {:db (assoc-in db [:auth :error] "")
      :firebase/login-user {:email email
                            :password password}})))

(reg-event-db
 :login-user-success
 validate-spec
 (fn [db [_ uid]]
   (-> (merge db app-db)
       (assoc-in [:auth :user] uid))))

(reg-event-db
 :login-user-fail
 validate-spec
 (fn [db [_]]
   (-> db
       (assoc-in [:auth :password] "")
       (assoc-in [:auth :user] nil)
       (assoc-in [:auth :error] "Authentication Failed!"))))

(reg-event-db
 :login-screen
 validate-spec
 (fn [db [_]]
   (login-screen db)))
