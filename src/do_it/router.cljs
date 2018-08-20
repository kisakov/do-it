(ns do-it.router
  (:require [cljs-react-navigation.base :as base]
            [cljs-react-navigation.re-frame :refer [init-state nil-fn ref-getStateForAction] :as nav]
            [re-frame.core :refer [dispatch subscribe]]))

(defn router [{:keys [root-router init-route-name add-listener]
               :or {add-listener nil-fn init-route-name :start-route}
               :as props}]
  (let [routing-sub (subscribe [::nav/routing-state])
        getStateForAction (aget root-router "router" "getStateForAction")]
    (reset! ref-getStateForAction getStateForAction)
    (fn [props]
      (let [routing-state (or @routing-sub
                              (init-state root-router init-route-name))]
        [:> root-router {:navigation
                         (base/addNavigationHelpers
                          (clj->js {:state    routing-state
                                    :addListener add-listener
                                    :dispatch (fn [action]
                                                (let [next-state (getStateForAction action routing-state)]
                                                  (dispatch [::nav/swap-routing-state next-state])))}))}]))))
