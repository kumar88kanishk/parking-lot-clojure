(ns parking-lot.core)
(def default-lot-state {:floors []
                        :veh-size {:cycle 1
                                   :car 2
                                   :bus 4}})

(defn create-parking-lot [m n lot-state]
  (assoc lot-state :floors (repeat m (repeat n nil))))

(defn get-veh-weight [veh-type lot-state] (get-in lot-state [:veh-size (keyword veh-type)]))


(defn park-on-floor [weight veh-num floor-space]
  (loop [current-spaces floor-space
         prevs []
         avail []]

    (if (empty? current-spaces)
      (concat prevs avail)
      (if (nil? (first current-spaces))
                                  ;; space is available
        (let [new-avail (conj avail (first current-spaces))]
          (if (= (count new-avail) weight)
            (concat prevs (repeat weight {:num veh-num}) (next current-spaces))
            (recur (next current-spaces) prevs (concat avail (take 1 current-spaces)))))
                                  ;; space not found
        (recur (next current-spaces)
               (concat prevs avail (take 1 current-spaces))
               [])))))

(defn park [lot-state veh-type veh-num]
  (let [weight (get-veh-weight veh-type lot-state)
        new-floors (loop [upcoming-floors (:floors lot-state)
                          prev-floors []]
                     (if upcoming-floors
                       (let [current-floor (first upcoming-floors)
                             new-floor (park-on-floor weight veh-num current-floor)]
                         (if (= current-floor new-floor)
                           (recur (next upcoming-floors) (conj prev-floors current-floor))
                           (concat prev-floors [new-floor] (rest upcoming-floors))))
                       prev-floors))]
    (if (= new-floors (:floors lot-state))
      [false lot-state]
      [true (assoc lot-state :floors new-floors)])))

(defn unpark [lot-state veh-num]
  (let [new-floors (map (fn [floor]
                          (map
                           (fn [space]
                             (if (= (get space :num) veh-num)
                               nil
                               space))
                           floor))
                        (:floors lot-state))]
    (assoc lot-state :floors new-floors)))

(defn park-vehicle [lot-state type veh-num]
  (let [[did-park? new-lot-state] (park lot-state type veh-num)]
    (if did-park?
      (println "Parked " veh-num)
      (println "Not parked " veh-num))
    new-lot-state))


(comment
  (def lot-state-atom (atom (create-parking-lot 2 4 default-lot-state)))
  @lot-state-atom

  (swap! lot-state-atom park-vehicle "bus" "UP32DD3659")
  (swap! lot-state-atom park-vehicle "car" "CAAAR")
  (swap! lot-state-atom park-vehicle "car" "CAAAR1")
  (swap! lot-state-atom park-vehicle "bus" "BUS!1")
  (swap! lot-state-atom park-vehicle "cycle" "CYCLE1")
  (swap! lot-state-atom unpark "UP32DD3659"))

