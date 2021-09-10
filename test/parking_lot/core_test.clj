(ns parking-lot.core-test
  (:require [clojure.test :refer [is deftest testing]]
            [parking-lot.core :as pl]))

(deftest create-parking-lot-test
  (testing "Create Parking Lot"
    (is (= {:0 [nil]} (pl/create-parking-lot 1 1)))))

(deftest get-veh-weight-test
  (testing "Get Vehicle Weight based on type"
    (is (= 2 (pl/get-veh-weight "car")))))

(deftest search-parking-floor-test
  (testing "Search available parking floor"
    (let [parking-lot (pl/create-parking-lot 1 1)
          floor (pl/search-parking-floor 1 parking-lot)]
      (is (= :0 floor)))))

(deftest new-parking-state-test
  (testing "Park vehicle and update state"
    (let [parking-lot-state (pl/create-parking-lot 1 2)
          park (pl/new-parking-state 2 "UP32ABC" parking-lot-state)]
      (is (= {:0 ["UP32ABC" "UP32ABC"]} park)))))