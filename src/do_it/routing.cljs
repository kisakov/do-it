(ns do-it.routing
  (:require [cljs-react-navigation.re-frame :refer [stack-navigator stack-screen] :as nav]
            [do-it.screens.login-form :refer [login-form]]
            [re-frame.core :refer [dispatch-sync subscribe]]
            [reagent.core :as r]
            [do-it.router :refer [router]]))

(def ReactNavigation (js/require "react-navigation"))

(def login-stack
  (stack-navigator {:login {:screen (stack-screen login-form {:title "Please Login"})}}
                   {:cardStyle {:backgroundColor "#fff"}}))

(def all-routes-stack (stack-navigator {:LoginStack {:screen login-stack}}
                                       {:headerMode "none"}))

(defn app-root []
  (r/create-class
   (let [nav-state (subscribe [::nav/routing-state])]
     {:component-will-mount (fn []
                              (when-not @nav-state
                                (dispatch-sync [:reset-routing-state])))
      :reagent-render       (fn []
                              [router {:root-router all-routes-stack}])})))


