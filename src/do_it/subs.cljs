(ns do-it.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :get-login
 (fn [db [_ prop]]
   (get-in db [:auth prop])))
