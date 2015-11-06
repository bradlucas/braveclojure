(ns clojure-noob.chapter9
  (:gen-class))

(defmacro postfix-notation
  "Convert infix notcation to postfix. Put the prefix function last"
  [expression]
  ;; conj builds a list by prepending each argument
  (conj (butlast expression) (last expression)))

(defmacro code-critic
  "phrases are courtesy Hermes Conrad from Futurama"
  [{:keys [good bad]}]
  (list 'do
        (list 'println 
              "Great squid of Madrid, this is bad code:" 
              (list 'quote bad))
        (list 'println 
              "Sweet goriall of Manila, this is good code:" 
              (list 'quote good))))

(defmacro foo
  [expression]
  (list 'quote expression))

(defmacro my-when
  [test & body]
  (list 'if test (cons 'do body)))

(defmacro my-unless
  "If !test execute body"
  [test & body]
  (conj (reverse body) test 'if))


;; syntax-quote
(defmacro code-critic2
  "phrases are courtesy Hermes Conrad from Futurama"
  [{:keys [good bad]}]
  `(do (println "Great squid of Madrid, this is bad code:" (quote ~bad))
       (println "Sweet goriall of Manila, this is good code:" (quote ~good))))


;; use syntax-quote because it lets you write things out more concisely and you're unquoting the bits that we want evaluated.


(defn criticize-code
  [criticism code]
  `(println ~criticism (quote ~code)))

(defmacro code-critic3
  [{:keys [good bad]}]
  `(do ~(criticize-code "Cursed bacteria of Liberia, this is bad code:" bad)
       ~(criticize-code "Sweet sacred boa of Western and Eastern Samoa, this is good code:" good)))



(defmacro code-critic-map
  [{:keys [good bad]}]
  `(do ~@(map #(apply criticize-code %)
             [["Great squid of Madrid, this is bad code:" bad]
              ["Sweet gorilla of Manila, this is good code:" good]])))

;; Without unquote splicing
`(+ ~(list 1 2 3))
; => (clojure.core/+ (1 2 3))

;; With unquote splicing
`(+ ~@(list 1 2 3))
; => (clojure.core/+ 1 2 3)

;; think of unquote splicing as unwrapping a seqable data structure, placing its contents directly within the enclosing syntax-quoted data structure.


(def message "Good job!")

;; variable capture. note that message is created
(defmacro with-mischief
  [& stuff-to-do]
  (concat (list 'let ['message "Oh, big deal!"])
          stuff-to-do))

(defmacro without-mischief
  [& stuff-to-do]
  (let [macro-message (gensym 'message)]
    `(let [~macro-message "Oh, big deal!"]
       ~@stuff-to-do
       (println "I still need to say: " ~macro-message))))
;; 
(def shipping-details
  {:name "Mitchard Blimmons"
   :address "134 Wonderment Ln"
   :city ""
   :state "FL"
   :postal-code "32501"
   :email "mitchard.blimmonsgmail.com"})

(def shipping-details-validations
  {:name
   ["Please enter a name" not-empty]
   :address
   ["Please enter an address" not-empty]
   :city
   ["Please enter a city" not-empty]
   :postal-code
   ["Please enter a postal code" not-empty
    "Please enter a postal code that looks like a postal code"
    #(or (empty? %)
         (not (re-seq #"[^0-9-]" %)))]
   :email
   ["Please enter an email address" not-empty
    "Your email address doesn't look like an email address"
    (or #(empty? %)
        #(re-seq #"@" %))]})



;; The validate function can be decomposed into two functions, one to apply validations to a single field and another to accumulate those error messages into a final map of error messages like the ones above.

;; Now we need to accumulate these error messages in a map:

(defn validate
  "returns a map with a vec of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))

;; Here's a function for applying validations to a single value:
(defn error-messages-for
  "return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))


;; typical usuage
;; (let [errors (validate shipping-details shipping-details-validation)]
;;   (if (empty? errors)
;;     (render :success)
;;     (render :failure errors)))

;; (let [errors (validate shipping-details shipping-details-validation)]
;;   (if (empty? errors)
;;     (do (save-shipping-details shipping-details)
;;         (redirect-to (url-for :order-confirmation)))
;;     (render "shipping-details" {:errors errors})))

(defmacro if-valid
  "Handle validation more concisely"
  [to-validate validations errors-name & then-else]
  `(let [~errors-name (validate ~to-validate ~validations)]
     (if (empty? ~errors-name)
       ~@then-else)))
  

;; with if-valid macro
;; (if-valid shipping-details shipping-details-validation errors
;;  (render :success)
;;  (render :failure errors))

;; (if-valid shipping-details shipping-details-validation errors
;;  (do (save-shipping-details shipping-details)
;;      (redirect-to (url-for :order-confirmation)))
;;  (render "shipping-details" {:errors errors}))
