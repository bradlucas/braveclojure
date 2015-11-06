(ns clojure-noob.chapter6
  (:gen-class))

;; Functional Programming

(defn sum
  ([values] (sum values 0))
  ([values total]
   (if (empty? values)
     total
     (sum (rest values) (+ (first values) total)))))

(defn sum-recur
  ([values] (sum values 0))
  ([values total]
   (if (empty? values)
     total
     (recur (rest values) (+ (first values) total)))))


(require '[clojure.string :as s])
(defn clean
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))



(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))


;; http://stackoverflow.com/a/21372291
(defn my-comp
  [& fns]
  (fn [& args]
    (reduce (fn [result-so-far next-fn] (next-fn result-so-far))
            (apply (last fns) args) (rest (reverse fns)))))

(defn my-comp
  [& fns]
  (fn [& args]
    (let [ordered-fns (reverse fns)
          first-result (apply (first ordered-fns) args)
          remaining-fns (rest ordered-fns)]
      (reduce 
       (fn [result-so-far next-fn] (next-fn result-so-far))
       first-result
       remaining-fns))))


(defn my-comp
  [& fns]
  (fn [& args]
    (let [ordered-fns (reverse fns)
          first-result (apply (first ordered-fns) args)
          remaining-fns (rest ordered-fns)]
      (reduce 
       (fn [result-so-far next-fn] (next-fn result-so-far))
       first-result
       remaining-fns))))




(defn sleepy-identity
  [x]
  (Thread/sleep 1000)
  x)

(def memo-sleepy-identity (memoize sleepy-identity))
