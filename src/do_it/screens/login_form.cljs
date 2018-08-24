(ns do-it.screens.login-form
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [do-it.components.common :refer [card card-section input button spinner]]))

(def ReactNative (js/require "react-native"))
(def text (r/adapt-react-class (.-Text ReactNative)))

(def error-style
  {:font-size 18
   :padding 10
   :color "red"})

(defn login-form []
  (let [email (subscribe [:get-login :email])
        password (subscribe [:get-login :password])
        error (subscribe [:get-login :error])
        uid (subscribe [:get-login :user-uid])
        loading (subscribe [:get-login :loading])]
    (fn []
      [card
       [card-section
        [input {:label "Email"
                :placeholder "email@gmail.com"
                :on-change-text #(dispatch-sync [:login-field-update :email %])
                :value email}]]
       [card-section
        [input {:label "Password"
                :placeholder "password"
                :secure-text-entry true
                :on-change-text #(dispatch-sync [:login-field-update :password %])
                :value password}]]
       (when-not (clojure.string/blank? @error)
         [text {:style error-style} @error])
       [text @uid]
       [card-section
        (if @loading
          [spinner]
          [button #(dispatch [:login-user]) "Login"])]])))
