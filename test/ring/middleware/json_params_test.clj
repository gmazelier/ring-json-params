(ns ring.middleware.json-params-test
  (:use clojure.test)
  (:use ring.middleware.json-params)
  (:require [clojure.contrib.io :as io])
  (:import java.io.ByteArrayInputStream))

(defn stream [s]
  (ByteArrayInputStream. (.getBytes s "UTF-8")))

(def json-echo
  (wrap-json-params identity))

(deftest noop-with-other-content-type
  (let [req {:content-type "application/xml"
             :body (stream "<xml></xml>")
             :params {"id" 3}}
        resp (json-echo req)]
    (is (= "<xml></xml>") (io/slurp* (:body resp)))
    (is (= {"id" 3} (:params resp)))
    (is (nil? (:json-params resp)))))

(deftest augments-with-json-content-type
  (let [req {:content-type "application/json; charset=UTF-8"
             :body (stream "{\"foo\": \"bar\"}")
             :params {"id" 3}}
        resp (json-echo req)]
    (is (= {"id" 3 "foo" "bar"} (:params resp)))
    (is (= {"foo" "bar"} (:json-params resp)))))

(deftest augments-with-vnd-json-content-type
  (let [req {:content-type "application/vnd.foobar+json; charset=UTF-8"
             :body (stream "{\"foo\": \"bar\"}")
             :params {"id" 3}}
        resp (json-echo req)]
    (is (= {"id" 3 "foo" "bar"} (:params resp)))
    (is (= {"foo" "bar"} (:json-params resp)))))

(deftest augments-without-overriding-existing-params
  (let [req {:content-type "application/json; charset=UTF-8"
             :body (stream "{\"foo\": \"bar\", \"key\": \"another\"}")
             :params {"id" 3 "key" "value"}}
        resp (json-echo req)]
    (is (= {"id" 3 "foo" "bar" "key" "value"} (:params resp)))
    (is (= {"foo" "bar" "key" "another"} (:json-params resp)))))

(deftest augments-with-json-array
  (let [req {:content-type "application/json; charset=UTF-8"
             :body (stream "[{\"foo\": \"bar\"}, {\"foo\": \"baz\"}]")
             :params {"id" 3}}
        resp (json-echo req)]
    (is (contains? (:params resp) :json-array))
    (is (= {"id" 3 :json-array [{"foo" "bar"} {"foo" "baz"}]} (:params resp)))
    (is (= [{"foo" "bar"} {"foo" "baz"}] (:json-params resp)))))
