(ns clojure-tictactoe.move-handler
  (:import MinMaxPlayer
           Move)
  (:use clojure.data.json))

(def x-token "X")
(def o-token "O")

(defn win-state [board]
  (let [game (MinMaxPlayer. board o-token x-token)]
    (cond
      (.maxWins game)     "O Wins"
      (.minWins game)     "X Wins"
      (.boardIsFull game) "Tie"
      :elsewise           "in progress")))

(defn move-to-index [move]
  (+ (* 3 (.getRow move))
     (.getColumn move)))

(defn make-new-board [board move]
  (let [game (MinMaxPlayer. board o-token x-token)]
    (if (or (.minWins game) (.boardIsFull game))
      board
      (do
        (aset board (move-to-index move) o-token)
        board))))

(defn decode-char [char]
  (cond
    (= \_ char) nil
    (= \X char) x-token
    (= \O char) o-token))

(defn string-to-board [string]
  (->> (.substring string 1)
       (map decode-char)
       (into-array String)))

(defn move-handler [env]
  (let [board       (string-to-board (:uri env))
        ai-move     (.nextMove (MinMaxPlayer. board o-token x-token))
        array-board (make-new-board board ai-move)
        win-state   (win-state array-board)]
    {:status  200
     :headers {}
     :body    (.getBytes
                (json-str {:newBoard array-board
                           :winState win-state}))}))

