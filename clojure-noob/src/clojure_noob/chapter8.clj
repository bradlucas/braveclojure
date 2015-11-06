(ns clojure-noob.chapter8
  (:gen-class))

(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))

(defmacro backwards
  [form]
  (reverse form))
