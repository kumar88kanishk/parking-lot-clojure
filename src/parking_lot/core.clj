(ns parking-lot.core)

(def parking-lot-state (atom {}))

(def parking-lot-bindings
  {:cycle 1
   :car 2
   :bus 4})

(defn create-parking-lot [m n]
  (swap! parking-lot-state (fn [s]
                             (reduce (fn [coll item]
                                       (assoc coll (keyword (str item)) (into [] (take n (repeat nil)))))
                                     {}
                                     (range m)))))

(defn veh-weight [veh-type] (get parking-lot-bindings (keyword veh-type)))

(defn search-level [weight]
  (let [search-level (reduce-kv
                      (fn [acc k v]
                        (let [availability (count (filter nil? v))]
                          (if (<= weight availability) (reduced k) acc)))
                      nil
                      @parking-lot-state)]
    search-level))

(defn new-parking-state [weight veh-num cs]
  (loop [current cs
         prevs []
         avail []]

    (if (empty? current)
      (concat prevs avail)
      (if (nil? (first current))
                                  ;; space is available
        (let [new-avail (conj avail (first current))]
          (if (= (count new-avail) weight)
            (concat prevs (repeat weight {:num veh-num}) (next current))
            (recur (next current) prevs (concat avail (take 1 current)))))
                                  ;; space not found
        (recur (next current)
               (concat prevs avail (take 1 current))
               [])))))

(defn park [veh-type veh-num]
  (if-let [weight (veh-weight veh-type)]
    (if-let [space (search-level weight)]
      (let [updated-state (update-in @parking-lot-state [space]
                                     (fn [current-state] (into [] (new-parking-state weight veh-num current-state))))]
        (swap! parking-lot-state (fn [old-state] (merge old-state updated-state))))
      "No Space")
    "Invalid Vehicle Type"))

(defn exit-park [veh-num]
  (let [updated-state (reduce-kv
                       (fn [acc k v]
                         (assoc acc k (mapv
                                       (fn [item] (if (= (get item :num) veh-num)
                                                    nil
                                                    item))
                                       v)))
                       {}
                       @parking-lot-state)]
    (if (= @parking-lot-state updated-state)
      "No Vehicle Found"
      (swap! parking-lot-state (fn [old-state] (merge old-state updated-state))))))

(comment

  ;; (assoc {} (keyword (str 1)) (into [] (range 4)))

  @parking-lot-state
  (swap! parking-lot-state (fn [state] (apply assoc state [:0 [] :1 []])))

  (create-parking-lot 4 4)
  (map (fn [[k v]] (park k v)) [{:bus "UP32DD5000"} {:car "UP32DD123"} {:bus "UP32DD1234"} {:bus "UP32DD12345"}])
  (park "bus" "UP32DD5000")

  (exit-park "UP32DD5000")

  (map (fn [a] (for [[k v] a] (print k v)))
       [{:bus "UP32DD5000"} {:car "UP32DD123"} {:bus "UP32DD1234"} {:bus "UP32DD12345"}])
  

  (key (seq {:a "b"}))

  (key (apply clojure.lang.MapEntry. {:bus "UP32DD5000"}))
  )
