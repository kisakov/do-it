(ns do-it.screens.login-form
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [do-it.components.common :refer [card card-section input button]]))

(def ReactNative (js/require "react-native"))
(def text (r/adapt-react-class (.-Text ReactNative)))

(def error-style
  {:font-size 20
   :align-self "center"
   :color "red"})

(defn login-form []
  (let [email (subscribe [:get-login :email])
        password (subscribe [:get-login :password])
        error (subscribe [:get-login :error])]
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
       [text {:style error-style} @error]
       [card-section
        [button #(dispatch [:login-user]) "Login"]]])))
