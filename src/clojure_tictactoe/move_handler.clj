(ns clojure-tictactoe.move-handler
  (:import MinMaxPlayer
           Move))

(def x "X")
(def o "O")

(defn str-to-token [token]
  (if (= token \_)
    nil
    token))

(defn char-to-token [character]
  (cond
    (= character nil) nil
    (= (str character) x) x
    (= (str character) o) o))

(defn token-to-str [token]
  (if (= nil token)
    "_"
    token))

(defn string-to-board [string]
  (->> (.substring string 1)
    (map str-to-token)
    (map char-to-token)
    (into-array String)))

(defn move-to-index [move]
  (let [row    (.getRow move)
        column (.getColumn move)]
    (+ (* row 3) column)))

(defn new-board [board move]
  (let [index (move-to-index move)
        token "O"
        board (vec board)]
  (println (.toString board))
  (->> (assoc board index token)
    (map token-to-str)
    (reduce str ""))))

(defn move-handler [env]
  (let [board   (string-to-board (:uri env))
        ai-move (.nextMove (MinMaxPlayer. board "O" "X"))]
    {:status 200
     :headers {}
     :body (new-board board ai-move)}))

