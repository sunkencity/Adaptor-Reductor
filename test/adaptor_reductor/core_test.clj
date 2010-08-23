(ns adaptor-reductor.core-test
  (:use [adaptor-reductor.core] :reload-all)
  (:use [clojure.test]))

(deftest sequence-without-adapter-test
  (is (= (sequence-without-adapter "FOOBAR" "FOOFAR" 3) "BAR")))
