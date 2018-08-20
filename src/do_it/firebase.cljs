(ns do-it.firebase
  (:require-macros [do-it.env :as env :refer [cljs-env]])
  (:require [re-frame.core :refer [reg-fx dispatch]]))

(def firebase (js/require "firebase/app"))
(def firebase-auth (js/require "firebase/auth"))
(def firebase-database (js/require "firebase/database"))

(defn init []
  (-> (cljs-env :firebase)
      clj->js
      firebase.initializeApp))

(defn- create-user [email password]
  (fn [] (-> (firebase.auth)
             (.createUserWithEmailAndPassword email password)
             (.catch #(dispatch [:login-user-fail])))))

(reg-fx
 :firebase/auth-user
 (fn []
   (.onAuthStateChanged (firebase.auth)
                        (fn [user]
                          (if user
                            (dispatch [:login-user-success (.-uid user)])
                            (dispatch [:login-screen]))))))

(reg-fx
 :firebase/login-user
 (fn [{:keys [email password]}]
   (-> (firebase.auth)
       (.signInWithEmailAndPassword email password)
       (.catch (create-user email password)))))

(reg-fx
 :firebase/logout-user
 (fn []
   (-> (firebase.auth)
       (.signOut))))

(defn- prepare-value [value]
  (clj->js
   (assoc value :createdAt (.-TIMESTAMP firebase.database.ServerValue))))

(reg-fx
 :firebase/push
 (fn [{:keys [reference value]}]
   (-> (firebase.database)
       (.ref reference)
       (.push (prepare-value value))
       (.then (dispatch [:employee-create-success])))))

(reg-fx
 :firebase/set
 (fn [{:keys [reference value]}]
   (-> (firebase.database)
       (.ref reference)
       (.set (prepare-value value))
       (.then (dispatch [:employee-create-success])))))

(reg-fx
 :firebase/remove
 (fn [{:keys [reference value]}]
   (-> (firebase.database)
       (.ref reference)
       (.remove)
       (.then (dispatch [:employee-create-success])))))
