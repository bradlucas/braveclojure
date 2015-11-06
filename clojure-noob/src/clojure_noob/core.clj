(ns clojure-noob.core
  (:require [clojure-noob.chapter5 :as c5]
            [clojure-noob.chapter6 :as c6]
            [clojure-noob.chapter8 :as c8]
            [clojure-noob.chapter9 :as c9])
  (:gen-class))

(defn -main
  [& args]
  (println "\n\nClojure Noob\n\n"))

(defn train
  []
  (println "True"))

(defn foo
  "Multiple arity version"
  ([x]
   x)
  ([] (foo 0)))

(defn my-first
  [[first-thing]]
  first-thing)

(defn chooser 
  [[first second]]
  (println first)
  (println second))

(defn location
  [{lat :lat lng :lng}]
  (println (str "lat " lat))
  (println (str "lng " lng)))

(defn next-location
  [{:keys [lat lng] :as location}]
  (println (str "lat:" lat))
  (println (str "lng:" lng)))

(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))


(def asym-hobbit-body-parts [{:name "head" :size 3}
                            {:name "left-eye" :size 1}
                            {:name "left-ear" :size 1}
                            {:name "mouth" :size 1}
                            {:name "nose" :size 1}
                            {:name "neck" :size 2}
                            {:name "left-shoulder" :size 3}
                            {:name "left-upper-arm" :size 3}
                            {:name "chest" :size 10}
                            {:name "back" :size 10}
                            {:name "left-forearm" :size 3}
                            {:name "abdomen" :size 6}
                            {:name "left-kidney" :size 1}
                            {:name "left-hand" :size 2}
                            {:name "left-knee" :size 2}
                            {:name "left-thigh" :size 4}
                            {:name "left-lower-leg" :size 3}
                            {:name "left-achilles" :size 1}
                            {:name "left-foot" :size 2}])

(defn needs-matching-part? 
  [part]
  (re-find #"^left-" (:name part)))

(defn make-matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-") :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps which have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts
            final-body-parts (conj final-body-parts part)]
        (if (needs-matching-part? part)
          (recur remaining (conj final-body-parts (make-matching-part part)))
          (recur remaining final-body-parts))))))

(defn process-lista
  "Example using recur to process a list. "
  [list]
  (loop [remaining-values list
         final-values []]
    (if (empty? remaining-values)
      final-values
      (let [[value & remaining] remaining-values
            final-values (conj final-values value)]
        (if (= 1 value)
          (recur remaining (conj final-values 'X'))
          (recur remaining final-values))))))

(defn my-reduce
  ([f initial coll]
   (loop [result initial
          remaining coll]
     (if (empty? remaining)
       result
       (recur (f result (first remaining)) (rest remaining)))))
  ([f [head & tail]]
   (my-reduce f head tail)))

(defn better-symmetrize-body-parts
  "Expects a seq of maps which have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (let [final-body-parts (conj final-body-parts part)]
              (if (needs-matching-part? part)
                (conj final-body-parts (make-matching-part part))
                final-body-parts)))
          []
          asym-body-parts))

(defn hit 
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + 0 (map :size sym-parts))  ;; sum all sizes
        target (inc (rand body-part-size-sum))] ;; random number blow sum all sizes
    (loop [[part & rest] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur rest (+ accumulated-size (:size part)))))))




