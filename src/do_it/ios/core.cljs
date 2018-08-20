(ns do-it.ios.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch-sync]]
            [do-it.events]
            [do-it.subs]
            [do-it.routing :as routing]
            [do-it.firebase :as firebase]))

(def ReactNative (js/require "react-native"))
(def app-registry (.-AppRegistry ReactNative))
(def yellow-box (.-YellowBox ReactNative))

(def app-root routing/app-root)

(defn init []
  (.ignoreWarnings yellow-box (clj->js ["Warning: isMounted(...) is deprecated"]))
  (dispatch-sync [:initialize-db])
  (firebase/init)
  (.registerComponent app-registry "DoIt" #(r/reactify-component app-root)))
