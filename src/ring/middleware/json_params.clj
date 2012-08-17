(ns ring.middleware.json-params
  (:require [clj-json.core :as json]))

(defn- json-request?
  [req]
  (if-let [#^String type (:content-type req)]
    (not (empty? (re-find #"^application/(vnd.+)?json" type)))))

(defn wrap-json-params [handler]
  (fn [req]
    (if-let [body (and (json-request? req) (:body req))]
      (let [bstr (slurp body)
            json-params (json/parse-string bstr)
            params (if (vector? json-params) {:json-array json-params} json-params)
            req* (assoc req
                   :json-params json-params
                   :params (merge params (:params req)))]
        (handler req*))
      (handler req))))
