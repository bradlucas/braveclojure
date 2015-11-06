(ns fwpd.core
  (:require [clojure.string :as s])
  (:gen-class))

(defn -main
  []
  (println "fwpd"))

(def filename "suspects.csv")

(defn str->int
  "Convert string to int"
  [str]
  (Integer. str))

(def headers->keywords {"Name" :name 
                        "Glitter Index" :glitter-index})

;; csv is all text, but we're storing numeric data.
;; we want to convert it back to actual numbers
(def conversions {:name identity 
                  :glitter-index str->int})

(defn parse
  "Convert a csv into rows of columns
  Returns a list of vectors with the data values as items
  ([\"Name\" \"Glitter Index\r\"] [\"Edward Cullen\" \" 10\r\"])"
  [string]
  (map #(s/split % #",")
       (map #(s/trim %) (s/split string #"\n"))))

(defn mapify
 "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (let [;; headers become the seq (:name :glitterindex)
        headers (map #(get headers->keywords %) (first rows))
        ;; unmapped-rows become the seq
        unmapped-rows (rest rows)]
    ;; return seq of 
    (map (fn [unmapped-row]
           (into {}
                 (mapify-row headers unmapped-row)
                 ))
         unmapped-rows)))


;; (map (fn [header column] [header column]) headers row2)
(defn mapify-row
  [headers unmapped-row]
  (map (fn [header column]
         [header ((get conversions header) column)]
         )
       headers
       unmapped-row))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))


;; print all as csv
(defn print-glitter-filter-csv
  [num]
  (map println (map (fn [row] (clojure.string/join ","  [(:name row) (:glitter-index row)])) (glitter-filter num rows))))
