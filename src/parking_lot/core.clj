(ns parking-lot.core)

(def parking-lot-state (atom {}))

(def parking-lot-bindings
  {:cycle 1
   :car 2
   :bus 4})
(defn create-parking-lot [m n]
  (swap! parking-lot-state (fn [s]
                             (reduce (fn [coll item]
                                       (assoc coll (keyword (str item)) (into [] (take n (repeat 1)))))
                                     {}
                                     (range m)))))

(defn park [veh-type veh-num]
  (let [weight (get parking-lot-bindings (keyword veh-type))
        search-space (reduce-kv
                      (fn [acc k v]
                        (let [availability (reduce + v)]
                          (if (< weight availability) (reduced (conj acc k)) (conj acc k))))
                      []
                      @parking-lot-state)]
    search-space))




(defn park-vehicles [queue]
  (let [weighted-queue (map
                        #(cond (= % "car") 2
                               (= % "bus") 4
                               (= % "cycle") 1)
                        queue)]
    weighted-queue))

(comment

  ;; (assoc {} (keyword (str 1)) (into [] (range 4)))

  @parking-lot-state
  (swap! parking-lot-state (fn [state] (apply assoc state [:0 [] :1 []])))

  (create-parking-lot 4 4)

  (park "car" "UP32DY3659")

  (park-vehicles ["car" "bus" "cycle"])
  (reduce (fn [coll item]
            (assoc coll (keyword (str item)) (into [] (range 4))))
          {}
          (range 4))
  (swap! parking-lot-state (fn [state] {}))
  (mapv (fn [a]) (range 4)))
