(ns parking-lot.core-test
  (:require [clojure.test :refer [is deftest testing]]
            [parking-lot.core :as pl]))

(deftest create-parking-lot-test
  (testing "Create Parking Lot"
    (is (= {:floors '((nil))
            :veh-size {:cycle 1
                       :car 2
                       :bus 4}} (pl/create-parking-lot 1 1 {:floors []
                                                            :veh-size {:cycle 1
                                                                       :car 2
                                                                       :bus 4}})))))


(deftest park-test
  (testing "Park Vehicle"
    (is (= [true {:floors '(({:num "UP3213123"} {:num "UP3213123"})), :veh-size {:cycle 1, :car 2, :bus 4}}]
           (pl/park {:floors '((nil nil))
                     :veh-size {:cycle 1
                                :car 2
                                :bus 4}} "car" "UP3213123")))))


(comment
  (pl/create-parking-lot 1 1 {:floors []
                              :veh-size {:cycle 1
                                         :car 2
                                         :bus 4}})
  (seq (seq nil))
  (clojure.test/run-tests))