(ns do-it.env
  (:require [environ.core :refer [env]]
            [clojure.edn :as edn]))

(defmacro cljs-env [kw]
  (edn/read-string (env kw)))
