(ns clojure-noob.chapter5
  (:gen-class))

;; Core Functions in Depth
;; http://www.braveclojure.com/core-functions-in-depth/

(defn foo5
  [] 
  (println "five"))

(defn label-key-val
  [[key val]]
  (str "key: " key ", val: " val))



(into {} (map (fn [[key val]] [key (inc val)]) 
     {:max 30 :min 10}))


(def human-consumption [8.1 7.3 6.6 5.0])
(def critter-consuption [0.0 0.2 0.3 1.1])
(defn unify-diet-data 
  [human critter]
  {:human human :critter critter})

;; #156 4Clojure.com
;; http://www.4clojure.com/problem/156
;; Map defaults
(defn build-map
  [num keys]
  (loop [[first & rest] keys
         final {}]
    (if (empty? rest)
      (conj final {first num})
      (recur rest (conj final {first num})))))


;; http://www.4clojure.com/problem/26
(defn fib
  [num]
  (loop [lst [1 1]]
    (if (< (count lst) num)
      (recur (conj lst (+  (last lst) (nth lst (- (count lst) 2)))))
      lst)))


;; 
(def sum #(reduce + %))    ;; sum a list using reduce
(def avg #(/ (sum %) (count %)))  ;; avg = sum / number of items

(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))



(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true :name "McMackson"}
   2 {:makes-blood-puns? true, :has-pulse? false :name "Damon Salmon"}
   3 {:makes-blood-puns? true, :has-pulse? true :name "Mickey Mouse"}})


(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)   ;; sleep 1000 = 1 second
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))))

(defn identify-vampire
  [social-security-number]
  (first (filter vampire?
                (map vampire-related-details social-security-number))))

(defn identify-humans
  [social-security-number]
  (filter #(not (vampire? %))
                (map vampire-related-details social-security-number)))

(def not-vampire? (complement vampire?))
(defn identify-humans2
  [social-security-number]
  (filter not-vampire?
                (map vampire-related-details social-security-number)))

(defn my-complement
  [func]
  (fn [& args]
    (not (apply func args))))

(defn even-numbers
  ([] (even-numbers 0)) 
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))


(defn my-conj
  [target & values]
  (into target values))

(defn my-into
  [target values]
  (apply conj target values))


(defn my-partial
  [partial-fn & args]
  (fn [& more-args]
    (apply partial-fn (into more-args (reverse args)))))
