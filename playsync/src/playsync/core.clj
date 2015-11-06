(ns playsync.core
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread 
                     alts! alts!! timeout]])
  )



(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))



;; create a channel called echo-chan
(def echo-chan (chan))

;;  go block - will run concurrently on a separate thread.
;; <! is the take function. It listens to the channel you give it as an argument, causing the process to wait until another process puts a message on the channel.
(go (println (<! echo-chan)))


;; put the message "ketchup" on the channel
(>!! echo-chan "ketchip")


;;  The simple answer is that you can use one exclamation point inside go blocks, but you have to use two exclamation points outside:

;;           inside go block	outside
;; put	     >! or >!!	          >!!
;; take	     <! or <!!	          <!!



(defn hotdog-machine
  []
  (let [in (chan)
        out (chan)]
    (go (<! in)
        (>! out "hotdog"))
    [in out]))


(defn hotdog-machine-v2
  [hotdog-count]
  (let [in (chan)
        out (chan)]
    (go (loop [hc hotdog-count]
          (if (> hc 0)
            (let [input (<! in)]
              (if (= 3 input)
                (do (>! out "hotdog")
                    (recur (dec hc)))
                (do (>! out "wilted lettuce")
                    (recur hc))))
            (do (close! in)
                (close! out)))))
    [in out]))



(defn my-reverse
  [& args]
  (if args
    (conj () (my-reverse (rest args)))
    nil))
